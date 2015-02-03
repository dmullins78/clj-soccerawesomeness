(ns cljawesome.schedule.models.teams
  (require [clojure.data.csv :as csv]
           [clojure.set :refer :all]
           [clj-time.coerce :as c]
           [cljawesome.util.filter :as util]
           [cljawesome.schedule.models.query-defs :as query]
           [cljawesome.league.models.query-defs :as league]
           [clojure.java.io :as io]))

(defn add-team-for-league [incoming leagueId]
  (let [existing (league/insert-team<! {:name incoming })]
    (league/insert-league-team<! {:leagueId leagueId :teamId (:id existing)} )))

(defn add-team-for-season [seasonId teamId division]
  (league/insert-season-team<! {:seasonId seasonId :teamId teamId :division division} ))

(defn add-team-for-league-season [incoming leagueId seasonId]
  (let [existing (add-team-for-league (:name incoming) leagueId)]
    (add-team-for-season seasonId (:team_id existing) (:division incoming))))

(defn find-team [incoming-team-name existing-teams]
  (util/find-first incoming-team-name existing-teams :name))

(defn load-teams [incoming-teams existing-teams leagueId seasonId]
  (doseq [incoming incoming-teams]
    (if-let [existing (find-team (:name incoming) existing-teams)]
      (add-team-for-season seasonId (:id existing) (:division incoming))
      (add-team-for-league-season incoming leagueId seasonId))))

(defn existing-teams [leagueId]
  (query/all-teams-by-league { :leagueId leagueId }))

(defn find-division [team incoming-teams]
  (:division (util/find-first team incoming-teams :home)))

(defn get-teams [schedule]
  (let [team (set (flatten (map #(vals (select-keys % [:home :away])) schedule)))]
    (map #(hash-map :name % :division (find-division % schedule)) team)))

(defn load-and-return [schedule leagueId seasonId]
  (let [incoming (get-teams schedule)
        existing (existing-teams leagueId)]
    (load-teams incoming existing leagueId seasonId )
    (query/all-teams-by-league { :leagueId leagueId })))
