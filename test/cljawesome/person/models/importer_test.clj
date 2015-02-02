(ns cljawesome.person.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.person.models.importer :refer :all]))

(facts "Load players"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Loads all players and assigns them to this season"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "Fall" :league_id leagueId}) :id)]
               (import-people leagueId seasonId "players.csv")
               (let [actual (query/select-season-players {:seasonId seasonId } )]
                 (count actual) => 2)))))
