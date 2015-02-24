(ns cljawesome.players.routes.players-routes-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [cljawesome.league.test-data :as dummy]
            [clj-time.coerce :as c]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.players.models.query-defs :as p]
            [cljawesome.core.handler :refer :all]))

(facts "Get the active season for a league"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Add player game stats"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "spring" leagueId)
                     teamOneId (dummy/team "Recipe" leagueId seasonId)
                     teamTwoId (dummy/team "Other" leagueId seasonId)
                     playerId (dummy/player-for-team "foo@foo.com" seasonId teamOneId)
                     gameId (dummy/game teamOneId 2 teamTwoId 3 seasonId)]
                 (let [uri (format "/cics/spring/games/%s/players/%s" gameId playerId)
                       response ( app (mock/content-type (mock/request :put uri
                                                                       (clojure.data.json/write-str {:goals 2 :assists 4 :card "Y"}))
                                                         "application/json"))
                       stats (dbtools/player-game-stats playerId)]
                   (:status response) => 200
                   (:goals stats) => 2
                   (:assists stats) => 4
                   (:card stats) => "Y")))))

