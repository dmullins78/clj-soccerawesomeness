(ns cljawesome.schedule.models.importer
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [cljawesome.schedule.models.query-defs :as query]
           [cljawesome.league.models.query-defs :as league]
           [clojure.java.io :as io]))

(defn add_season [leagueId season]
  ((league/insert-season<! {:year 2014 :season season :league_id leagueId}) :id))

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

(defn find-first-division
  [teamName allData]
  (:division (first (filter #(= (:home %) teamName) allData))))

(defn all_teams [schedule]
  (let [z (set (flatten (map #(vals (select-keys % [:home :away])) schedule)))]
    (map #(hash-map :name % :division (find-first-division % schedule)) z)))

(defn add_new_season_team [seasonId teamId division]
  (league/insert-season-team<! {:seasonId seasonId :teamId teamId :division division} ))

(defn add_new_league_team [team leagueId]
  (let [team (league/insert-team<! {:name team })]
    (league/insert-league-team<! {:leagueId leagueId :teamId (:id team)} )))

(defn brand-new-team [team leagueId seasonId]
  (let [newTeam (add_new_league_team (:name team) leagueId)]
    (add_new_season_team seasonId (:team_id newTeam) (:division team))))

(defn my_teams [leagueId]
  (query/find-league-teams { :leagueId leagueId }))

(defn find-first-team
  [teamName existingTeams]
  (first (filter #(= (:name %) teamName) existingTeams)))

(defn new_league_teams [new_teams leagueId]
  (let [existing_teams (my_teams new_teams leagueId)]
    (difference new_teams existing_teams )))

(defn loading-teams [all_new_teams existing_teams leagueId seasonId]
  (doseq [x all_new_teams]
    (if-let [existing-team (find-first-team (:name x) existing_teams)]
      (add_new_season_team seasonId (:id existing-team) (:division existing-team))
      (brand-new-team x leagueId seasonId))))

(defn load_new_teams [schedule leagueId seasonId]
  (let [all_new_teams (all_teams schedule)
        existing_teams (my_teams leagueId)]
    (loading-teams all_new_teams existing_teams leagueId seasonId )
    (query/find-league-teams { :leagueId leagueId })))

(defn import_schedule [league_id season file]
  (let [schedule (load_things file)
        seasonId (add_season league_id season)
        teams (load_new_teams schedule league_id seasonId)]
    (doseq [game schedule]
      (let [home_team_id  ((find-first-team (:home game) teams) :id)
            away_team_id  ((find-first-team (:away game) teams) :id)
            start-time (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy hh:mm a") (:time game))]
        (league/insert-game<! {:home_team_id home_team_id :away_team_id away_team_id :home_team_score 0 :away_team_score 0 :start_time (c/to-sql-date start-time)} )))))

