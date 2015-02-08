(ns cljawesome.person.routes.person-routes
  (:require [compojure.core :refer :all]
            [cljawesome.util.league-params :as lp]
            [cljawesome.person.models.query-defs :as pq]
            [cljawesome.person.views.login-view :refer :all]
            [ring.util.response :refer [resource-response response]]))


(defn update-player-stats [league personId gameId params]
  (pq/insert-person-game-stats<!
    { :seasonId (:seasonid league)
     :personId (Integer. personId)
     :gameId (Integer. gameId)
     :yellow_card (Boolean. (:yellow_card params))
     :red_card (Boolean. (:red_card params))
     :assists (Integer. (:assists params))
     :goals (Integer. (:goals params)) })
  "")

(defroutes person-routes
  (POST "/persons/:name/:year/:season/:id/game/:gameId" {params :params}
        (update-player-stats (lp/parse-params params) (:id params) (:gameId params) params)))
