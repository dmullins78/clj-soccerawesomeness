(ns cljawesome.person.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [clj-time.core :as t]
           [cljawesome.person.models.query-defs :as query]
           [cljawesome.util.filter :as util]
           [cljawesome.schedule.models.query-defs :as teams]
           [clojure.java.io :as io]))

(defn existing-person? [person existing-people]
  (util/find-first (:email person) existing-people :email))

(defn emails [people]
  (map #(:email %) people))

(defn find-existing [incoming-people]
  (query/select-people-by-email { :emails (emails incoming-people) }))

(defn add-person [person seasonId teamId]
  (let [person (query/insert-person<! { :email (:email person) :name (:name person)})]
    (query/insert-person-season<! { :personId (:id person) :seasonId seasonId :teamId teamId})))

(defn read-file [in-file]
  (with-open [in-file (io/reader in-file)]
    (doall (csv/read-csv in-file))))

(defn to-person [records]
  (map #(hash-map
          :email (nth % 0)
          :name (nth % 1)
          :team (nth % 2)) records))

(defn parse-people [file]
  (-> file read-file to-person))

(defn import-people [league-id seasonId file]
  (let [incoming-people (parse-people file)
        existing-people (find-existing incoming-people)
        teams (teams/teams-by-season {:seasonId seasonId} )]
    (doseq [person incoming-people]
      (let [team (util/find-first (:team person) teams :name)]
        (if-let [existing-person (existing-person? person existing-people)]
          (query/insert-person-season<! { :teamId (:id team) :personId (:id existing-person) :seasonId seasonId})
          (add-person person seasonId (:id team)))))))



