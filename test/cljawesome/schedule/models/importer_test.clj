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
                     season (query/insert-season<! {:year 2015 :season "Spring" :league_id leagueId})
                     actual (import_schedule leagueId (:id season) "games.csv")]
                 (count actual) => 81))

         (fact "Do not create existing teams for subsequent seasons"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     season (query/insert-season<! {:year 2015 :season "Spring" :league_id leagueId})
                     teamOneId ((query/insert-team<! {:name "Recipe"}) :id) ]
                 (query/insert-league-team<! {:leagueId leagueId :teamId teamOneId} )
                 (import_schedule leagueId (:id season) "games.csv")
                 (let [actual (dbtools/entries-in-teams-table "Recipe" )]
                   actual => 1)))))
