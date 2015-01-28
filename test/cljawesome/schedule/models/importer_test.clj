(ns cljawesome.schedule.models.importer-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [cljawesome.database.database-helper :as dbtools]
            [cljawesome.league.models.query-defs :as query]
            [cljawesome.schedule.models.importer :refer :all]))

(facts "Creates a league from a schedule"

       (with-state-changes [(before :facts (dbtools/resetdb! ))]

         ;(fact "Gets the specific season"
               ;(let [leagueId ((query/insert-league<! {:name "CICS"}) :id)]
                 ;(let [seasonId ((query/insert-season<! {:year 2014 :season "spring" :league_id leagueId}) :id)]
                   ;(let [divisionId ((query/insert-division<! {:season_id seasonId :name "Upper"}) :id)]
                     ;(let [teamOneId ((query/insert-team<! {:name "Recipe"}) :id) ]
                         ;(query/insert-league-team {:leagueId leagueId :teamId teamOneId} )
                         ;(query/insert-season-team {:seasonId seasonId :teamId teamOneId :divisionId divisionId} ))
                     ;(let [teamOneId ((query/insert-team<! {:name "Beta"}) :id) ]
                         ;(query/insert-league-team {:leagueId leagueId :teamId teamOneId} )
                         ;(query/insert-season-team {:seasonId seasonId :teamId teamOneId :divisionId divisionId}))
               ;(let [response (import_schedule leagueId "Fall" "/Users/dan/Projects/clojure/cljawesome/in-file.csv")]
                 ;response => { :season "Fall" :teams ["Alpha" "Beta"]})
         ;))))
         
         ))
