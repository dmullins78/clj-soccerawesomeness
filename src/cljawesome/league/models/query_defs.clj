(ns cljawesome.league.models.query-defs
  (:require [environ.core :refer [env]]
            [cljawesome.league.models.normalizer :refer :all ]
            [cljawesome.league.models.points-calculator :refer :all ]
            [yesql.core :refer [defqueries]]))


(defqueries "cljawesome/league/models/league_queries.sql" {:connection (env :database-url)})

(defn get-season [league season]
  (first (select-league-season {:name league :season season})))

(def calc-game-points (comp calculate normalize))

(defn calculate-team-points [team]
  (let [games (select-games {:team_id (:id team) :seasonId (:seasonid team)})]
    (let [[points goal_diff scored allowed wins losses ties] (calc-game-points (:id team) games)]
      (merge team {:points points
                   :goal_differential goal_diff
                   :scored scored
                   :wins wins
                   :losses losses
                   :ties ties
                   :allowed allowed}))))

(defn get-team-standings [league season]
  (let [seasonMap (get-season league season)
        teams (select-season-teams {:seasonId (:seasonid seasonMap)})]
    (map calculate-team-points teams)))

