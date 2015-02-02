(ns cljawesome.person.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [clj-time.core :as t]
           [cljawesome.person.models.query-defs :as query]
           [cljawesome.schedule.models.query-defs :as teams]
           [clojure.java.io :as io]))

(defn read-file [in-file]
  (with-open [in-file (io/reader in-file) ]
    (doall (csv/read-csv in-file))))

(defn to-person [records]
  (map #(hash-map
          :email (nth % 0)
          :name (nth % 1)
          :team (nth % 2)) records))

(defn parse-people [file]
  (-> file read-file to-person))

(defn import-people [league-id seasonId file]
  (let [people (parse-people file)
        teams (teams/all-teams-by-league { :leagueId league-id })]
    (doseq [person people]
      (let [person (query/insert-person<! { :email (:email person) :name (:name person)})]
        (query/insert-person-season<! { :personId (:id person) :seasonId seasonId}))
      )))

