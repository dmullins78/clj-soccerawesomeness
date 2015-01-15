(ns cljawesome.league.models.points-calculator-test
  (:require [midje.sweet :refer :all]
            [cljawesome.league.models.points-calculator :refer :all]))

(fact "Failing test scenario"
      (def games
        [{:us 2, :them 1}
         {:us 2, :them 1}])

      (let [[points goal_diff] (calculate games)]
        points => 6
        goal_diff => 2))

(fact "Calculate the points for a team"
      (def games
        [{:us 3, :them 0}
         {:us 1, :them 1}
         {:us 0, :them 1}])

      (let [[points goal_diff] (calculate games)]
        points => 4
        goal_diff => 2))
