(ns cljawesome.core.handler
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [cljawesome.core.routes.league-routes :refer [league-routes]]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (route/not-found "Not Found"))

(def app
  (-> (routes league-routes app-routes)
    (middleware/wrap-json-body)
    (middleware/wrap-json-response)
    (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))

;(def app
;  (-> (handler/api app-routes)
;    (middleware/wrap-json-body)
;    (middleware/wrap-json-response)))