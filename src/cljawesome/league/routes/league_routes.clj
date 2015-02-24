(ns cljawesome.league.routes.league-routes
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [resource-response response]]
            [selmer.parser :refer [render-file]]
            [cljawesome.util.league-params :as lp]
            [cljawesome.league.models.query-defs :as query]))

(defn get-league [league year season]
  (let [league (query/select-league-season {:name league :year year :season season})]
    (response (first league) )))

(defn get-divisions [team]
  (:division team))

(defn soccer-scorer
  [t1 t2]
  (cond
    (> (:points t1) (:points t2)) true
    (< (:points t1) (:points t2)) false
    :else (> (:goal_differential t1) (:goal_differential t2))))

(defn get-teams [league season]
  (let [teams (query/get-team-standings league season)
        lg (query/select-league-by-name {:name league})
        sorted-teams (sort (comp soccer-scorer) teams)
        sorted-divisions (group-by :division sorted-teams)]
    (render-file "league.html" {:league (first lg) :teams sorted-divisions :base (lp/basepath league season)})))

(defroutes league-routes
  (GET  "/:league/:season" [league season] (get-teams league season)))
