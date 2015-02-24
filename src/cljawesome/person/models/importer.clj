(ns cljawesome.person.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [clj-time.core :as t]
           [cljawesome.person.models.query-defs :as query]
           [cljawesome.util.filter :as util]
           [cljawesome.schedule.models.query-defs :as teams]
           [clojure.java.io :as io]))

(defn existing-player? [player existing-players]
  (util/find-first (:email player) existing-players :email))

(defn emails [players]
  (map #(:email %) players))

(defn find-existing [incoming-players]
  (query/select-players-by-email { :emails (emails incoming-players) }))

(defn add-player [player seasonId teamId]
  (let [player (query/insert-player<! { :email (:email player) :name (:name player)})]
    (query/insert-player-season<! { :playerId (:id player) :seasonId seasonId :teamId teamId})))

(defn read-file [in-file]
  (with-open [in-file (io/reader in-file)]
    (doall (csv/read-csv in-file))))

(defn to-player [records]
  (map #(hash-map
          :email (nth % 0)
          :name (nth % 1)
          :team (nth % 2)) records))

(defn parse-players [file]
  (-> file read-file to-player))

(defn reset-season-rosters [seasonId]
  (query/reset-season-roster<! {:seasonId seasonId}))

(defn import-players [league-id seasonId file]
  (let [incoming-players (parse-players file)
        existing-players (find-existing incoming-players)
        teams (teams/teams-by-season {:seasonId seasonId} )]
    (reset-season-rosters seasonId)
    (doseq [player incoming-players]
      (let [team (util/find-first (:team player) teams :name)]
        (if-let [existing-player (existing-player? player existing-players)]
          (query/insert-player-season<! { :teamId (:id team) :playerId (:id existing-player) :seasonId seasonId})
          (add-player player seasonId (:id team)))))))



