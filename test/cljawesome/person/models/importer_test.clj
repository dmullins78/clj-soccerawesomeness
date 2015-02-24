(ns cljawesome.person.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.test-data :as dummy]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.person.models.query-defs :as p]
            [cljawesome.person.models.importer :refer :all]))

(facts "Load players"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Loads all players and assigns them to this season"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "fall" leagueId)]
                 (dummy/team "Recipe" leagueId seasonId)
                 (import-players leagueId seasonId "players.csv")
                 (let [actual (query/select-season-players {:seasonId seasonId } )]
                   (count actual) => 2
                   (:team (first actual)) => "Recipe")))

         (fact "Do not load existing players twice"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "fall" leagueId)
                     teamId (dummy/team "Recipe" leagueId seasonId)
                     player (dummy/player "one@foo.com")]
                 (import-players leagueId seasonId "players.csv")
                 (let [actual (dbtools/players-with-name "one@foo.com" )
                       season-player-count (dbtools/players-by-season seasonId )]
                   actual => 1
                   season-player-count => 2)))))
