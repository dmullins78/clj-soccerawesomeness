(ns cljawesome.players.routes.players-routes
  (:require [compojure.core :refer :all]
            [cljawesome.util.league-params :as lp]
            [cljawesome.players.models.query-defs :as pq]
            [cljawesome.league.models.query-defs :as league]
            [ring.util.response :refer [resource-response response]]))


(defn update-player-stats [league playerId gameId body]
  (pq/insert-player-game-stats<!
    { :seasonId (:seasonid league)
     :playerId (Integer. playerId)
     :gameId (Integer. gameId)
     :card (:card body)
     :assists (Integer. (:assists body))
     :goals (Integer. (:goals body)) })
  "")

(defroutes players-routes
  (PUT "/:name/:season/games/:gameId/players/:playerId" {params :params body :body}
       (league/delete-player-game-stats<! {:gameId (Integer. (:gameId params)) :playerId (Integer. (:playerId params))})
       (update-player-stats (lp/parse-params params) (:playerId params) (:gameId params) body)))
