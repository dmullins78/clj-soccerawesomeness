(ns cljawesome.schedule.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.schedule.models.importer :refer :all]))

(facts "Creates a league from a schedule"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Makes new league from schedule import"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     newSeasonId (import_schedule leagueId "Fall" "games.csv")
                     actual (query/select-season-games {:seasonId newSeasonId } )]
                 (count actual) => 81))

         (fact "Do not create existing teams for subsequent seasons"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     teamOneId ((query/insert-team<! {:name "Recipe"}) :id) ]
                 (query/insert-league-team<! {:leagueId leagueId :teamId teamOneId} )
                 (let [newSeasonId (import_schedule leagueId "Fall" "games.csv")
                       actual (dbtools/entries-in-teams-table "Recipe" )]
                   actual => 1)))))
