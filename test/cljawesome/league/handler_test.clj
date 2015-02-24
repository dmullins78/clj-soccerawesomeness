(ns cljawesome.league.handler-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [cljawesome.league.test-data :as dummy]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.core.handler :as handler]))

(facts "Get the active season for a league"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Gets teams from the active session and calculates their points"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "fall" leagueId)
                     teamOneId (dummy/team "Recipe" leagueId seasonId)
                     teamTwoId (dummy/team "Red Star" leagueId seasonId)]
                 (dummy/game teamOneId 2 teamTwoId 1 seasonId )
                 (dummy/game teamTwoId 1 teamOneId 2 seasonId )
                 (handler/init)
                 (let [response (handler/app (mock/request :get "/cics/spring"))]
                   (:status response) => 200)))))
