(ns cljawesome.person.routes.person-routes-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.person.models.query-defs :as p]
            [cljawesome.core.handler :refer :all]))

(facts "Get the active season for a league"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Add player game stats"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "spring" :league_id leagueId}) :id)
                     teamOneId ((query/insert-team<! {:name "Recipe"}) :id)
                     teamTwoId ((query/insert-team<! {:name "Other"}) :id)
                     game (query/insert-game<! {:home_team_id teamOneId :away_team_id teamTwoId :home_team_score 2 :away_team_score 1 :start_time (c/to-sql-date (t/now)) :field "Altoona", :seasonId seasonId} )
                     personId ((p/insert-person<! {:name "Test" :email "foo@foo.com"}) :id)]
                 (query/insert-season-team<! {:seasonId seasonId :teamId teamOneId :division "Upper"} )
                 (p/insert-person-season<! {:seasonId seasonId :teamId teamOneId :personId personId} )

                 (let [uri (format "/games/cics/2014/spring/%s/players/%s" (:id game) personId)
                       response ( app (mock/request :post uri {:goals 2 :card "Y" :assists 4 }))
                       stats (dbtools/player-game-stats personId)]
                   (:status response) => 200
                   (:goals stats) => 2
                   (:card stats) => "Y")))))

