(ns cljawesome.teams.routes.teams-routes
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [selmer.filters :refer :all]
            [cljawesome.util.league-params :as lp]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as teams]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.data.json :as json]
            [ring.util.response :as ring-resp]
            [clojure.string :refer [lower-case]]
            [ring.util.response :refer [resource-response response]]))

(defn base-path [league]
  (lower-case (format "%s/%s/%s" (:league league)(:year league)(:season league))))

(defn update-game-params [gameId params]
  {:home_score (Integer. (:home_score params))
   :away_score (Integer. (:away_score params))
   :id (Integer. gameId)})

(defn update-game [league gameId params request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (league/update-game<! (update-game-params gameId params))
  (redirect (format "/teams/%s/%s/games/%s" (base-path league) (:teamId params) gameId)))

(defn get-players-for-game [gameId]
  (league/get-players-for-game {:gameId (Integer. gameId)}))

(defn delete-player-game [gameId personId request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (league/delete-player-game-stats<! {:gameId (Integer. gameId) :personId (Integer. personId)}))

(defn game-permissions [user]
  (cond
    (nil? user) {:details "disabled" :score "disabled"}
    (= "teamadmin" (:role user)) {:details "disabled" :update true}
    (= "leagueadmin" (:role user)) {:update true}))

(defn show-game [league gameId user]
  (let [game (first (league/select-game {:gameId (Integer. gameId) }))
        players (league/players-by-teams {:teamIds [(:home_team_id game) (:away_team_id game)] })
        permissions (game-permissions user)]
    (println permissions)
    (render-file "game.html" {:game game :players players :base (base-path league) :permissions permissions})))

(defn show-team-games [league teamId]
  (let [games (league/select-games {:team_id (Integer. teamId) :seasonId (Integer. (:seasonid league)) })]
    (render-file "games.html" {:games games :teamId teamId :base (base-path league)})))

(defn show-teams [league]
  (let [teams (teams/teams-by-season {:seasonId (:seasonid league)})]
    (render-file "teams-list.html" {:teams teams :base (base-path league)})))

(defroutes teams-routes
  (GET "/games/:name/:year/:season/:id" {session :session params :params} (show-game (lp/parse-params params) (:id params) (:identity session)))
  (GET "/games/:name/:year/:season/:gameId/players" [gameId] (get-players-for-game gameId ))
  (DELETE "/games/:name/:year/:season/:gameId/players/:personId" [gameId personId :as {request :request}] (delete-player-game gameId personId request))
  (POST "/teams/:name/:year/:season/:teamId/games/:id" {params :params request :request} (update-game (lp/parse-params params) (:id params) params request))
  (GET "/teams/:name/:year/:season/:id/games" {params :params} (show-team-games (lp/parse-params params) (:id params) ))
  (GET "/teams/:name/:year/:season" {params :params} (show-teams (lp/parse-params params) )))
