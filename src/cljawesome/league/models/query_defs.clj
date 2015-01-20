(ns cljawesome.league.models.query-defs
  (:require [environ.core :refer [env]]
            [cljawesome.league.models.normalizer :refer :all ]
            [cljawesome.league.models.points-calculator :refer :all ]
            [yesql.core :refer [defqueries]]))

(defqueries "cljawesome/league/models/league_queries.sql" {:env database-url})

(def calc-game-points (comp calculate normalize))

(defn calculate-team-points [team]
  (let [games (select-games {:team_id (:id team)} {:env database-url})]
    (let [[points goal_diff] (calc-game-points (:id team) games)]
      (merge team {:points points :goal_differential goal_diff}))))

(defn get-team-standings [league year season]
  (let [teams (select-teams {:name league :year year :season season} {:env database-url})]
    (map calculate-team-points teams)))
