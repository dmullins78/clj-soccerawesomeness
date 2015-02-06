(ns cljawesome.teams.routes.teams-routes
  (:require [compojure.core :refer :all]
            [selmer.parser :refer [render-file]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as teams]
            [ring.util.response :as ring-resp]
            [clojure.java.io :as io]
            [ring.util.response :refer [resource-response response]]))

(selmer.parser/set-resource-path! (.getAbsolutePath (io/as-file "./resources/templates")))

(defn parse-params [params]
  (league/season-id
    (get params :name)
    (get params :year)
    (get params :season)))

(defn show-teams [league]
  (let [teams (teams/teams-by-season {:seasonId (:seasonid league)})]
    (render-file "teams-list.html" {:teams teams })))

(defroutes teams-routes
  (GET "/league/:name/:year/:season/teams2" {params :params} (show-teams (parse-params params) )))
