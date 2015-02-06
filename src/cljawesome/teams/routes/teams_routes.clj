(ns cljawesome.teams.routes.teams-routes
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as teams]
            [ring.util.response :as ring-resp]
            [clojure.string :refer [lower-case]]
            [ring.util.response :refer [resource-response response]]))

(defn parse-params [params]
  (league/season-id
    (get params :name)
    (get params :year)
    (get params :season)))

(defn base-path [league]
  (lower-case (format "%s/%s/%s" (:league league)(:year league)(:season league))))

(defn show-team-games [league teamId]
  (let [games (league/select-games {:team_id (Integer. teamId) :seasonId (Integer. (:seasonid league)) })]
    (render-file "games.html" {:games games :teamId teamId :base (base-path league)})))

(defn show-teams [league]
  (let [teams (teams/teams-by-season {:seasonId (:seasonid league)})]
    (render-file "teams-list.html" {:teams teams :base (base-path league)})))

(defroutes teams-routes
  (GET "/team/:name/:year/:season/:id/games" {params :params} (show-team-games (parse-params params) (:id params) ))
  (GET "/teams/:name/:year/:season" {params :params} (show-teams (parse-params params) )))
