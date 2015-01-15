(ns cljawesome.league.handler-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cljawesome.core.models.database :refer [database db]]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.core.handler :refer :all]))

(def test-db (database :test))

(facts "Get the active season for a league"

    (with-state-changes [(before :facts (dbtools/resetdb! ))]

        (fact "Gets the specific season"
            (with-redefs [db test-db]
                (let [leagueId ((query/insert-league<! {:name "CICS"} {:connection test-db}) :id)]
                    (query/insert-season<! {:year 2014 :season "fall" :league_id leagueId} {:connection test-db})
                    (query/insert-season<! {:year 2014 :season "spring" :league_id leagueId} {:connection test-db}))
                (let [response (app (mock/request :get "/league/cics/2014/fall"))]
                  (:status response) => 200
                  (:body response) => "{\"season\":\"fall\",\"year\":2014,\"league\":\"CICS\"}")))

        (fact "Gets teams from the active session and calculates their points"
            (with-redefs [db test-db]
                (let [leagueId ((query/insert-league<! {:name "CICS"} {:connection test-db}) :id)]
                    (let [seasonId ((query/insert-season<! {:year 2014 :season "spring" :league_id leagueId} {:connection test-db}) :id)]
                        (let [divisionId ((query/insert-division<! {:season_id seasonId :name "Upper"} {:connection test-db}) :id)]
                            (let [teamOneId ((query/insert-team<! {:name "Recipe" :division_id divisionId :season_id seasonId} {:connection test-db}) :id) ]
                            (let [teamTwoId ((query/insert-team<! {:name "Red Star" :division_id divisionId :season_id seasonId} {:connection test-db}) :id) ]
                                (query/insert-game<! {:home_team_id teamOneId :away_team_id teamTwoId :home_team_score 2 :away_team_score 1} {:connection test-db})
                                (query/insert-game<! {:home_team_id teamTwoId :away_team_id teamOneId :home_team_score 1 :away_team_score 2} {:connection test-db}))))))
                (let [response (app (mock/request :get "/league/cics/2014/spring/teams"))]
                  (:status response) => 200
                  (:body response) => (contains "goal_differential\":2" )
                  (:body response) => (contains "points\":6" ) )))))
