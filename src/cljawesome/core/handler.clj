(ns cljawesome.core.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [cljawesome.league.routes.league-routes :refer [league-routes]]
            [cljawesome.person.routes.person-routes :refer [person-routes]]
            [cljawesome.schedule.routes.import-routes :refer [schedule-routes]]
            [cljawesome.teams.routes.teams-routes :refer [teams-routes]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(defroutes app-routes
  (route/not-found "Not Found"))

(defn init []
  (selmer.parser/set-resource-path! (.getAbsolutePath (io/as-file "./resources/templates"))))

(def app
  (-> (routes schedule-routes league-routes person-routes teams-routes app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
