(ns cljawesome.schedule.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.schedule.models.importer :refer :all]))

(facts "Creates a league from a schedule"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         (fact "Gets the specific season"
               (let [leagueId ((query/insert-league<! {:name "CICS"}) :id)
                     seasonId ((query/insert-season<! {:year 2014 :season "spring" :league_id leagueId}) :id)
                     teamOneId ((query/insert-team<! {:name "Recipe"}) :id)
                     teamTwoId ((query/insert-team<! {:name "Red Star" }) :id) ]
                 (query/insert-season-team<! {:seasonId seasonId :teamId teamOneId :division "Upper"} )
                 (query/insert-league-team<! {:leagueId leagueId :teamId teamOneId} )
                 (query/insert-season-team<! {:seasonId seasonId :teamId teamTwoId :division "Upper"} )
                 (query/insert-league-team<! {:leagueId leagueId :teamId teamTwoId} )
               (let [response (import_schedule leagueId "Fall" "/Users/dan/Projects/clojure/cljawesome/in-file.csv")]
                 response => { :season "Fall" :teams ["Alpha" "Beta"]})))))
