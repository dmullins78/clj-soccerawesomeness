(ns cljawesome.core.models.normalizer-test
  (:require [midje.sweet :refer :all]
            [cljawesome.core.models.normalizer :refer :all]))

(def games
  [{:home_team_id 1, :ht_score 1, :away_team_id 2, :at_score 0}
   {:home_team_id 8, :ht_score 1, :away_team_id 1, :at_score 3}])

(fact "Home and away games are normalized"
      (let [outcome (normalize 1 games)]
        (:us (first outcome)) => 1
        (:them (first outcome)) => 0
        (:us (second outcome)) => 3
        (:them (second outcome)) => 1))

