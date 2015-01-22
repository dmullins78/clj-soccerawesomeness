(ns cljawesome.league.routes.league-routes
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [resource-response response]]
            [cljawesome.league.models.query-defs :as query]))

(defn league-params [request]
  [(get-in request [:params :league-name])
   (Integer. (get-in request [:params :year]))
   (get-in request [:params :season])])

  (defn get-league [request]
    (let [[league year season] (league-params request)]
      (let [league (query/select-league-season {:name league :year year :season season})]
        (response (first league) ))))

  (defn get-teams [request]
    (let [[league year season] (league-params request)]
      (let [teams (query/get-team-standings league year season)]
        (response teams ))))

  (defroutes league-routes
    (GET  "/league/:league-name/:year/:season" [] get-league)
    (GET  "/league/:league-name/:year/:season/teams" [] get-teams))
