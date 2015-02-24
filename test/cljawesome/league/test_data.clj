(ns cljawesome.league.test-data
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.players.models.query-defs :as p]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.core.handler :refer :all]))

(defn league []
  ((query/insert-league<! {:path "cics" :name "Central Iowa Coed Soccer" :description "Desc" :location "Des Moines"}) :id))

(defn season [season leagueId]
  ((query/insert-season<! {:season season :leagueId leagueId}) :id))

(defn team [team leagueId seasonId]
  (let [teamId ((query/insert-team<! {:name "Recipe"}) :id)]
    (query/insert-season-team<! {:seasonId seasonId :teamId teamId :division "Upper"} )
    (query/insert-league-team<! {:leagueId leagueId :teamId teamId} )
    teamId))

(defn game [home home_score away away_score seasonId ]
  (:id (query/insert-game<! {:home_team_id home :away_team_id away :home_team_score home_score :away_team_score away_score :start_time (c/to-sql-date (t/now)) :field "Altoona", :seasonId seasonId} )))

(defn player [email]
  (:id (p/insert-player<! { :email email :name "Test One" })))

(defn player-for-team [email seasonId teamId]
  (let [playerId ((p/insert-player<! { :email email :name "Test One" }) :id)]
    (p/insert-player-season<! {:seasonId seasonId :teamId teamId :playerId playerId} )
  playerId))
