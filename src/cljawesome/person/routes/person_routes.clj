(ns cljawesome.person.routes.person-routes
  (:require [compojure.core :refer :all]
            [cljawesome.util.league-params :as lp]
            [cljawesome.person.models.query-defs :as pq]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.person.views.login-view :refer :all]
            [ring.util.response :refer [resource-response response]]))


(defn update-player-stats [league personId gameId body]
  (pq/insert-person-game-stats<!
    { :seasonId (:seasonid league)
     :personId (Integer. personId)
     :gameId (Integer. gameId)
     :card (:card body)
     :assists (Integer. (:assists body))
     :goals (Integer. (:goals body)) })
  "")

(defroutes person-routes
  (PUT "/:name/:year/:season/games/:gameId/players/:personId" {params :params body :body}
       (league/delete-player-game-stats<! {:gameId (Integer. (:gameId params)) :personId (Integer. (:personId params))})
       (update-player-stats (lp/parse-params params) (:personId params) (:gameId params) body)))
