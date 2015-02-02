(ns cljawesome.schedule.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [clj-time.core :as t]
           [cljawesome.schedule.models.teams :as teams]
           [cljawesome.schedule.models.query-defs :as query]
           [cljawesome.league.models.query-defs :as league]
           [clojure.java.io :as io]))

(defn add_season [leagueId season]
  (let [currentYear (t/year (t/now))]
    ((league/insert-season<! {:year currentYear :season season :league_id leagueId}) :id)))

(defn read-file [in-file]
  (with-open [in-file (io/reader in-file) ]
    (doall (csv/read-csv in-file))))

(defn to-games [records]
  (map #(hash-map
          :time (nth % 0)
          :field (nth % 1)
          :division (nth % 2)
          :home (nth % 4)
          :away (nth % 5)) records))

(defn parse-schedule [file]
  (-> file read-file to-games))


(defn import_schedule [league_id season file]
  (let [schedule (parse-schedule file)
        seasonId (add_season league_id season)
        teams (teams/load-and-return schedule league_id seasonId)]
    (doseq [game schedule]
      (let [home_team_id  ((teams/find-team (:home game) teams) :id)
            away_team_id  ((teams/find-team (:away game) teams) :id)
            start-time (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy hh:mm a") (:time game))]
        (league/insert-game<! {
                               :home_team_id home_team_id
                               :away_team_id away_team_id
                               :home_team_score 0
                               :away_team_score 0
                               :field (:field game)
                               :seasonId seasonId
                               :start_time (c/to-sql-date start-time)} )))
    seasonId))

