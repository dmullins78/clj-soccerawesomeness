(ns cljawesome.league.models.query-defs
  (:require [environ.core :refer [env]]
            [cljawesome.league.models.normalizer :refer :all ]
            [cljawesome.league.models.points-calculator :refer :all ]
            [yesql.core :refer [defqueries]]))


(defqueries "cljawesome/league/models/league_queries.sql" {:connection (env :database-url)})

(def calc-game-points (comp calculate normalize))

(defn calculate-team-points [team]
  (let [games (select-games {:team_id (:id team)})]
    (let [[points goal_diff] (calc-game-points (:id team) games)]
      (merge team {:points points :goal_differential goal_diff}))))

(defn get-team-standings [league year season]
  (let [teams (select-teams {:name league :year year :season season})]
    (map calculate-team-points teams)))
