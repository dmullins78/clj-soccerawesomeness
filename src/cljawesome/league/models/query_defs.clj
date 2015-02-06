(ns cljawesome.league.models.query-defs
  (:require [environ.core :refer [env]]
            [cljawesome.league.models.normalizer :refer :all ]
            [cljawesome.league.models.points-calculator :refer :all ]
            [yesql.core :refer [defqueries]]))


(defqueries "cljawesome/league/models/league_queries.sql" {:connection (env :database-url)})

(defn get-season [league year season]
  (first (select-league-season {:name league :year (Integer. year) :season season})))

(def calc-game-points (comp calculate normalize))

(defn calculate-team-points [team seasonId]
  (let [games (select-games {:team_id (:id team) :seasonId (Integer. seasonId)})]
    (let [[points goal_diff] (calc-game-points (:id team) games)]
      (merge team {:points points :goal_differential goal_diff}))))

(defn get-team-standings [league year season]
  (let [seasonMap (get-season league year season)
        teams (select-season-teams {:seasonId (:seasonid seasonMap)})]
    (map calculate-team-points teams (:seasonid seasonMap))))

