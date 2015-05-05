(ns cljawesome.teams.routes.teams-routes
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [selmer.filters :refer :all]
            [clj-time.coerce :as c]
            [cljawesome.admin.models.soccer-authentication :refer [notadmin? teamadmin? leagueadmin?]]
            [cljawesome.util.league-params :as lp]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as teams]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.data.json :as json]
            [ring.util.response :as ring-resp]
            [clojure.string :refer [lower-case]]
            [ring.util.response :refer [resource-response response]]))

(defn base-path [league]
  (lower-case (format "%s/%s" (:league league)(:season league))))

(defn game-score-params [gameId params]
  {:home_score (Integer. (:home_score params))
   :away_score (Integer. (:away_score params))
   :id (Integer. gameId)
   :comments (:comments params)})

(defn game-detail-params [gameId params]
  {:start_time (c/to-sql-time (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy hh:mm a") (:start_time params)))
   :field (:field params)
   :id (Integer. gameId) })

(defn update-details [gameId params redirect-path]
  (league/update-game-details<! (game-detail-params gameId params))
  (redirect redirect-path))

(defn update-scores [gameId params redirect-path]
  (league/update-game-scores<! (game-score-params gameId params))
  (redirect redirect-path))

(defn update-game-scores [league season teamId gameId request]
  (let [user (:identity request)
        redirect-path (lp/basepath-lead-slash league season "teams/" teamId)] 
    (when (not (authenticated? request))
      (throw-unauthorized {:message "Not authorized"}))
    (update-scores gameId (:params request) redirect-path)))

(defn update-game-details [league season teamId gameId request]
  (let [user (:identity request)
        redirect-path (lp/basepath-lead-slash league season "teams/" teamId)] 
    (when (not (leagueadmin? user))
      (throw-unauthorized {:message "Not authorized"}))
    (update-details gameId (:params request) redirect-path)))

(defn get-players-for-game [gameId]
  (league/get-players-for-game {:gameId (Integer. gameId)}))

(defn delete-player-game [gameId playerId request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (league/delete-player-game-stats<! {:gameId (Integer. gameId) :playerId (Integer. playerId)}))

(defn game-permissions [user]
  (cond
    (notadmin? user) {}
    (teamadmin? user) {:scores true }
    (leagueadmin? user) {:scores true :details true}))

(defn show-game-scores [league gameId teamId user]
  (let [game (first (league/select-game {:gameId (Integer. gameId) }))
        permissions (game-permissions user)]
    (render-file "game-scores.html" {:game game :permissions permissions :teamId teamId :base (base-path league)})))

(defn with-game-stats [player]
  (if-let [stats (league/get-player-stats {:seasonId (:seasonid player) :playerId (:id player)})]
    (merge player {:cards stats})
    player))

(defn show-team-players [league season teamId]
  (let [league (league/get-season league season)
        players (league/get-players { :teamId (Integer. teamId) :seasonId (:seasonid league)})
        teams (league/get-team {:teamId (Integer. teamId)})
        new-players (map with-game-stats players)]
    (render-file "players.html" {:players new-players :team (first teams) :base (base-path league)})))

(defn show-team [league season teamId user]
  (let [league (league/get-season league season)
        team (league/get-team {:teamId (Integer. teamId)})
        permissions (game-permissions user)
        games (league/select-games {:team_id (Integer. teamId) :seasonId (Integer. (:seasonid league)) })]
    (render-file "team.html" {:games games :permissions permissions :team (first team) :base (base-path league)})))

(defn show-game-details [lg season gameId teamId]
  (let [game (first (league/select-game {:gameId (Integer. gameId) }))
        league (league/get-season lg season)]
    (render-file "game-details.html" {:game game :teamId teamId :base (base-path league)})))

(defroutes teams-routes
  (GET "/:name/:season/games/:id" [name season id teamId] (show-game-details name season id teamId))
  (GET "/:name/:season/games/:id/scores" {session :session params :params} (show-game-scores (lp/parse-params params) (:id params) (:teamId params) (:identity session)))
  (GET "/:name/:season/games/:gameId/scores/players" [gameId] (get-players-for-game gameId ))
  (DELETE "/:name/:season/games/:gameId/scores/players/:playerId" [gameId playerId :as request] (delete-player-game gameId playerId request))
  (POST "/:league/:season/games/:gameId" [league season gameId teamId :as request] (update-game-details league season teamId gameId request))
  (POST "/:league/:season/games/:gameId/scores" [league season gameId teamId :as request] (update-game-scores league season teamId gameId request))
  (GET "/:league/:season/teams/:teamId" [league season teamId :as request] (show-team league season teamId (:identity request)))
  (GET "/:league/:season/teams/:teamId/players" [league season teamId] (show-team-players league season teamId)))
