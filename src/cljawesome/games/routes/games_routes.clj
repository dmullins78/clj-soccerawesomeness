(ns cljawesome.games.routes.games-routes
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

(defroutes games-routes
  (GET "/:name/:season/games" [name season] (show-add-game name season))
  (POST "/:league/:season/games" [league season gameId teamId :as request] (update-game-details league season teamId gameId request)))
