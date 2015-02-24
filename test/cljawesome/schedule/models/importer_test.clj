(ns cljawesome.schedule.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.league.test-data :as dummy]
            [cljawesome.schedule.models.importer :refer :all]))

(facts "Creates a league from a schedule"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Makes new league from schedule import"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "spring" leagueId)
                     actual (import_schedule leagueId seasonId "games.csv")]
                 (count actual) => 81))

         (fact "Do not create existing teams for subsequent seasons"
               (let [leagueId (dummy/league)
                     seasonId (dummy/season "spring" leagueId)
                     teamId (dummy/team "Recipe" leagueId seasonId)]
                 (import_schedule leagueId seasonId "games.csv")
                 (let [actual (dbtools/entries-in-teams-table "Recipe" )]
                   actual => 1)))))
