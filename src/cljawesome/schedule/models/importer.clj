(ns cljawesome.schedule.models.importer
  (require [clojure.data.csv :as csv]
           [cljawesome.schedule.models.query-defs :as query]
           [cljawesome.league.models.query-defs :as league]
           [clojure.java.io :as io]))

(defn league_teams[league_id]
  (query/all-teams-by-league {:league_id league_id} ))

(defn read_file [in-file]
  (with-open [in-file (io/reader in-file) ]
    (doall (csv/read-csv in-file))))

(defn make_objects [records]
  (map #(hash-map
          :time (nth % 0)
          :field (nth % 1)
          :division (nth % 2)
          :home (nth % 4)
          :away (nth % 5)) records))

(defn load_things [file]
  (-> file read_file make_objects))

(defn all_teams [schedule]
  (set (flatten (map #(vals (select-keys % [:home :away])) schedule))))

(defn team_match? [team team2]
  (= (:name team2) team))

(defn add_new_team [team seasonId]
  (league/insert-team<! {:name team :division_id (Integer. (add_division seasonId)) :season_id (Integer. seasonId)}))

(defn master_team_list [new_teams season]
  (let [leagueTeams (league_teams seasonId)]
  (doseq [team new_teams]
    (if (not (some #(team_match? team %) leagueTeams))
      (add_new_team team seasonId)))))

(defn import_schedule [league_id season file]
  (let [schedule (load_things file)]
    (let [new_teams (all_teams schedule)]
      (master_team_list new_teams  (add_season league_id season))
      )))

(defn add_season [leagueId season]
  ((league/insert-season<! {:year 2014 :season season :league_id leagueId}) :id))

(defn add_division [seasonId]
  ((league/insert-division<! {:season_id seasonId :name "Upper"}) :id))

