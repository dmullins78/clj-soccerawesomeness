(ns cljawesome.core.models.query-defs
  (:require [cljawesome.core.models.database :refer [database db]]
            [cljawesome.core.models.normalizer :refer :all ]
            [cljawesome.core.models.points-calculator :refer :all ]
            [yesql.core :refer [defqueries]]))

(defqueries "cljawesome/core/models/league_queries.sql" {:connection db})

(def calc-game-points (comp calculate normalize))

(defn calculate-team-points [team]
  (let [games (select-games {:team_id (:id team)} {:connection db})]
    (let [[points goal_diff] (calc-game-points (:id team) games)]
      (merge team {:points points :goal_differential goal_diff}))))

(defn get-team-standings [league year season]
  (let [teams (select-teams {:name league :year year :season season} {:connection db})]
    (map calculate-team-points teams)))
