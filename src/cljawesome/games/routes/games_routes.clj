(ns cljawesome.games.routes.games-routes
  (:use ring.util.response)
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [selmer.filters :refer :all]
            [clj-time.local :as l]
            [clj-time.format :as f]
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

(def starts-at-formatter (f/formatter "MM/dd/yyyy hh:mm a"))

(defn show-add-game [name season added]
  (let [league (league/get-season name season)
        teams (league/select-teams-by-season { :seasonId (:seasonid league)})
        game { :starts_at (f/unparse starts-at-formatter (l/local-now))}]
    (render-file "game-details.html" {:base (base-path league) :game game :teams teams :added added })))

(defn game-detail-params [seasonId params]
  {:start_time (c/to-sql-time (.parse (java.text.SimpleDateFormat. "MM/dd/yyyy hh:mm a") (:start_time params)))
   :field (:field params)
   :home_team_score 0
   :away_team_score 0
   :seasonId seasonId
   :home_team_id (Integer. (:home_team params))
   :away_team_id (Integer. (:away_team params))})

(defn add-game [name season params]
  (let [league (league/get-season name season)
        game-details (game-detail-params (:seasonid league) params)]
    (league/insert-game<! game-details)
    (redirect (str "/" (base-path league) "/games?added=true"))))

(defroutes games-routes
  (GET "/:name/:season/games" [name season added] (show-add-game name season added))
  (POST "/:name/:season/games" [name season :as request] (add-game name season (:params request))))
