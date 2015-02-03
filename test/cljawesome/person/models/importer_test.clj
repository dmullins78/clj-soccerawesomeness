(ns cljawesome.person.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.person.models.query-defs :as p]
            [cljawesome.person.models.importer :refer :all]))

(facts "Load players"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Loads all players and assigns them to this season"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "Fall" :league_id leagueId}) :id)
                     team (query/insert-team<! {:name "Recipe"})]
                 (query/insert-season-team<! {:teamId (:id team) :seasonId seasonId :division "U"})
                 (import-people leagueId seasonId "players.csv")
                 (let [actual (query/select-season-players {:seasonId seasonId } )]
                   (count actual) => 2
                   (:team (first actual)) => "Recipe")))

         (fact "Do not load existing players twice"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "Fall" :league_id leagueId}) :id)
                     team (query/insert-team<! {:name "Recipe"})
                     player (p/insert-person<! { :email "one@foo.com" :name "Test One" })]
                 (query/insert-season-team<! {:teamId (:id team) :seasonId seasonId :division "U"})
                 (import-people leagueId seasonId "players.csv")
                 (let [actual (dbtools/players-with-name "one@foo.com" )
                       season-player-count (dbtools/players-by-season seasonId )]
                   actual => 1
                   season-player-count => 2)))))
