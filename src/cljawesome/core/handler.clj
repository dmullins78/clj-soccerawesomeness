(ns cljawesome.core.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [ring.middleware.session :refer [wrap-session]]
            [cljawesome.league.routes.league-routes :refer [league-routes]]
            [cljawesome.players.routes.players-routes :refer [players-routes]]
            [cljawesome.schedule.routes.import-routes :refer [schedule-routes]]
            [cljawesome.login.routes.login-routes :refer [login-routes]]
            [cljawesome.teams.routes.teams-routes :refer [teams-routes]]
            [cljawesome.admin.routes.admin-routes :refer [admin-routes]]
            [compojure.response :refer [render]]
            [ring.util.response :refer [resource-response response]]
            [selmer.filters :refer [add-filter!]]
            [ring.middleware.json :as middleware]
            [clojure.java.io :as io]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(defroutes app-routes
  (route/not-found "Not Found"))

(defn unauthorized-handler [request metadata]
  (-> (render (slurp (io/resource "invalid.html")) request)
      (assoc :status 403)))

(add-filter! :cardlabel
             (fn [x] (if (= x "R") "Red" "Yellow")))

(defn init []
  (selmer.parser/set-resource-path! (.getAbsolutePath (io/as-file "./resources/templates"))))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))

(def app
  (-> (routes schedule-routes league-routes login-routes admin-routes players-routes teams-routes app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
