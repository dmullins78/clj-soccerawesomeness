(ns cljawesome.league.handler-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.core.handler :refer :all]))

(facts "Get the active season for a league"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Gets the specific season"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)]
                 (query/insert-season<! {:year 2014 :season "fall" :league_id leagueId})
                 (query/insert-season<! {:year 2014 :season "spring" :league_id leagueId}))
               (let [response (app (mock/request :get "/league/cics/2014/fall"))]
                 (:status response) => 200
                 (:body response) => "{\"season\":\"fall\",\"year\":2014,\"league\":\"CICS\"}"))

         (fact "Gets teams from the active session and calculates their points"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "spring" :league_id leagueId}) :id)
                     teamOneId ((query/insert-team<! {:name "Recipe"}) :id)]
                 (query/insert-season-team<! {:seasonId seasonId :teamId teamOneId :division "Upper"} )
                 (query/insert-league-team<! {:leagueId leagueId :teamId teamOneId} )
                 (let [teamTwoId ((query/insert-team<! {:name "Red Star" }) :id) ]
                   (query/insert-league-team<! {:leagueId leagueId :teamId teamTwoId} )
                   (query/insert-season-team<! {:seasonId seasonId :teamId teamTwoId :division "Upper"} )
                   (query/insert-game<! {:home_team_id teamOneId :away_team_id teamTwoId :home_team_score 2 :away_team_score 1 :start_time (c/to-sql-date (t/now))} )
                   (query/insert-game<! {:home_team_id teamTwoId :away_team_id teamOneId :home_team_score 1 :away_team_score 2 :start_time (c/to-sql-date (t/now))} )))
               (let [response (app (mock/request :get "/league/cics/2014/spring/teams"))]
                 (:status response) => 200
                 (:body response) => (contains "goal_differential\":2" )
                 (:body response) => (contains "points\":6" ) ))))
