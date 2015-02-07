(ns cljawesome.teams.routes.teams-routes
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as teams]
            [ring.util.response :as ring-resp]
            [clojure.string :refer [lower-case]]
            [ring.util.response :refer [resource-response response]]))

(defn parse-params [params]
  (league/get-season
    (get params :name)
    (get params :year)
    (get params :season)))

(defn update-game-params [gameId params]
  {:home_score (Integer. (:home_score params))
   :away_score (Integer. (:away_score params))
   :id (Integer. gameId)})

(defn base-path [league]
  (lower-case (format "%s/%s/%s" (:league league)(:year league)(:season league))))

(defn update-game [league gameId params]
  (league/update-game<!
    (update-game-params gameId params))
  (redirect
    (format "/teams/%s/%s/games/%s" (base-path league) (:teamId params) gameId)))

(defn show-game [league teamId gameId]
  (let [game (league/select-game {:gameId (Integer. gameId) })]
    (render-file "game.html" {:game (first game) :teamId teamId :base (base-path league)})))

(defn show-team-games [league teamId]
  (let [games (league/select-games {:team_id (Integer. teamId) :seasonId (Integer. (:seasonid league)) })]
    (render-file "games.html" {:games games :teamId teamId :base (base-path league)})))

(defn show-teams [league]
  (let [teams (teams/teams-by-season {:seasonId (:seasonid league)})]
    (render-file "teams-list.html" {:teams teams :base (base-path league)})))

(defroutes teams-routes
  (GET "/teams/:name/:year/:season/:teamId/games/:id" {params :params} (show-game (parse-params params) (:teamId params) (:id params)))
  (POST "/teams/:name/:year/:season/:teamId/games/:id" {params :params} (update-game (parse-params params) (:id params) params ))
  (GET "/teams/:name/:year/:season/:id/games" {params :params} (show-team-games (parse-params params) (:id params) ))
  (GET "/teams/:name/:year/:season" {params :params} (show-teams (parse-params params) )))
