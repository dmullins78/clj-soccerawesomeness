(ns cljawesome.schedule.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [cljawesome.schedule.models.query-defs :as query]
           [cljawesome.league.models.query-defs :as league]
           [clojure.java.io :as io]))

(defn add_season [leagueId season]
  ((league/insert-season<! {:year 2014 :season season :league_id leagueId}) :id))

(defn add_division [seasonId]
  ((league/insert-division<! {:season_id seasonId :name "Upper"}) :id))

(defn league_teams[league_id]
  (query/all-teams-by-league {:leagueId league_id} ))

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

(defn add_new_league_team [team leagueId]
  (let [team (league/insert-team<! {:name team })]
    (league/insert-league-team<! {:leagueId leagueId :teamId (:id team)} )))

(defn my_teams [new_teams leagueId]
  (let [matches (query/find-league-teams { :leagueId leagueId :names (seq new_teams)})]
    (map #(:name %) matches)))

(defn new_league_teams [new_teams leagueId]
  (let [existing_teams (my_teams new_teams leagueId)]
    (difference new_teams existing_teams )))

(defn load_new_teams [schedule leagueId seasonId]
  (let [all_new_teams (all_teams schedule)
        new-league-teams (new_league_teams all_new_teams leagueId)]
    (apply add_new_league_team new-league-teams leagueId)))

(defn import_schedule [league_id season file]
  (let [schedule (load_things file)
        seasonId (add_season league_id season)]
    (load_new_teams schedule league_id seasonId)))

