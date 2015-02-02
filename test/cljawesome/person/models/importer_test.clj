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
                     seasonId ((query/insert-season<! {:year 2014 :season "Fall" :league_id leagueId}) :id)]
                 (import-people leagueId seasonId "players.csv")
                 (let [actual (query/select-season-players {:seasonId seasonId } )]
                   (count actual) => 2)))

         (fact "Do not load existing players twice"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "Fall" :league_id leagueId}) :id)
                     player (p/insert-person<! { :email "one@foo.com" :name "Test One" })]
                 (import-people leagueId seasonId "players.csv")
                 (let [actual (dbtools/players-with-name "one@foo.com" )]
                   actual => 1)))))
