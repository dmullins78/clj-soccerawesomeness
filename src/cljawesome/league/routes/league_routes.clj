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
  (if (> (:points t1) (:points t2))
    true
    false))

(defn get-teams [league year season]
  (let [teams (query/get-team-standings league (Integer. year) season)
        sorted-teams (sort (comp soccer-scorer) teams)
        sorted-divisions (group-by :division sorted-teams)]
    (render-file "league.html" {:teams sorted-divisions :base (lp/basepath league year season)})))

(defroutes league-routes
  (GET  "/:league/:year/:season" [league year season] (get-teams league year season)))
