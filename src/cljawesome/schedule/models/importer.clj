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

;(defn league_teams[league_id]
  ;(query/all-teams-by-league {:leagueId league_id} ))

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

(defn add_new_season_team [seasonId team]
  ;(league/insert-season-team<! {:seasonId seasonId :teamId (:id team)} )
  )

(defn add_new_league_team [team leagueId]
  (let [team (league/insert-team<! {:name team })]
    (league/insert-league-team<! {:leagueId leagueId :teamId (:id team)} )))

(defn brand-new-team [team leagueId seasonId]
  (add_new_league_team team leagueId)
  (add_new_season_team seasonId team))

(defn my_teams [leagueId]
  (query/find-league-teams { :leagueId leagueId }))

(defn find-first-team
  [teamName existingTeams]
  (first (filter #(= (:name %) teamName) existingTeams)))

(defn new_league_teams [new_teams leagueId]
  (let [existing_teams (my_teams new_teams leagueId)]
    (difference new_teams existing_teams )))

(defn load_new_teams [schedule leagueId seasonId]
  (let [all_new_teams (all_teams schedule)
        existing_teams (my_teams leagueId)]
    (doseq [x all_new_teams]
      (if-let [y (find-first-team x existing_teams)]
        (add_new_season_team seasonId y)
        (brand-new-team x leagueId seasonId)
      ))))

(defn import_schedule [league_id season file]
  (let [schedule (load_things file)
        seasonId (add_season league_id season)]
    (load_new_teams schedule league_id seasonId)))

