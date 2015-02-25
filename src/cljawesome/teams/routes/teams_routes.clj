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
  (lower-case (format "%s/%s" (:league league)(:season league))))

(defn update-game-params [gameId params]
  {:home_score (Integer. (:home_score params))
   :away_score (Integer. (:away_score params))
   :home_team_id (Integer. (:home_team params))
   :away_team_id (Integer. (:away_team params))
   :field (:field params)
   :id (Integer. gameId)
   :comments (:comments params)})

(defn update-game [league season teamId gameId request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (league/update-game<! (update-game-params gameId (:params request)))
  (redirect (lp/basepath-lead-slash league season "games/" gameId (str "?teamId=" teamId))))

(defn get-players-for-game [gameId]
  (league/get-players-for-game {:gameId (Integer. gameId)}))

(defn delete-player-game [gameId playerId request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (league/delete-player-game-stats<! {:gameId (Integer. gameId) :playerId (Integer. playerId)}))

(defn game-permissions [user]
  (cond
    (nil? user) {:details "disabled" :score "disabled"}
    (= "teamadmin" (:role user)) {:details "disabled" :update true}
    (= "leagueadmin" (:role user)) {:update true}))

(defn show-game [league gameId teamId user]
  (let [game (first (league/select-game {:gameId (Integer. gameId) }))
        players (league/players-by-teams {:teamIds [(:home_team_id game) (:away_team_id game)] })
        teams (teams/teams-by-season {:seasonId (:seasonid league)})
        permissions (game-permissions user)]
    (render-file "game.html" {:game game :players players :teamId teamId :teams teams :base (base-path league) :permissions permissions})))

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

(defn show-team [league season teamId]
  (let [league (league/get-season league season)
        team (league/get-team {:teamId (Integer. teamId)})
        games (league/select-games {:team_id (Integer. teamId) :seasonId (Integer. (:seasonid league)) })]
    (render-file "team.html" {:games games :team (first team) :base (base-path league)})))

(defn show-teams [league]
  (let [teams (teams/teams-by-season {:seasonId (:seasonid league)})]
    (render-file "teams-list.html" {:teams teams :base (base-path league)})))

(defroutes teams-routes
  (GET "/:name/:season/games/:id" {session :session params :params} (show-game (lp/parse-params params) (:id params) (:teamId params) (:identity session)))
  (GET "/:name/:season/games/:gameId/players" [gameId] (get-players-for-game gameId ))
  (DELETE "/:name/:season/games/:gameId/players/:playerId" [gameId playerId :as request] (delete-player-game gameId playerId request))
  (POST "/:league/:season/games/:gameId" [league season gameId teamId :as request] (update-game league season teamId gameId request))
  (GET "/:league/:season/teams/:teamId" [league season teamId] (show-team league season teamId))
  (GET "/:league/:season/teams/:teamId/players" [league season teamId] (show-team-players league season teamId))
  (GET "/:name/:season/teams" {params :params} (show-teams (lp/parse-params params) )))
