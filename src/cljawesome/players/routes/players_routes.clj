(ns cljawesome.players.routes.players-routes
  (:require [compojure.core :refer :all]
            [cljawesome.util.league-params :as lp]
            [cljawesome.players.models.query-defs :as pq]
            [cljawesome.league.models.query-defs :as league]
            [ring.util.response :refer [resource-response response]]))


(defn findplayers [league season query]
  (let [lg (league/get-season league season)]
    (pq/find-players-by-name { :seasonId (:seasonid lg) :query (str "%" query "%")})))

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
  (GET "/:league/:season/players/all" [league season query] (findplayers league season query))
  (PUT "/:name/:season/games/:gameId/players/:playerId" {params :params body :body}
       (league/delete-player-game-stats<! {:gameId (Integer. (:gameId params)) :playerId (Integer. (:playerId params))})
       (league/game-was-modified<! { :gameId (Integer. (:gameId params)) })
       (update-player-stats (lp/parse-params params) (:playerId params) (:gameId params) body)))
