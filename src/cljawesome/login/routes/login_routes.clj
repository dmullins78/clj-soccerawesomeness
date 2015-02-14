(ns cljawesome.login.routes.login-routes
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [compojure.response :refer [render]]
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.adapter.jetty :as jetty]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn login [request]
  (render-file "login.html" {}))

(defn home [request]
  (println "Session " + (:session request))
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [session (:session request)]
    (render-file "home.html" {:person (:identity session)})))

(defn auth [email]
  {:name email + " Admin" :role email})

(defn login-authenticate
  [email password session]
  (let [session (assoc session :identity (auth email))]
    (-> (redirect "/home")
        (assoc :session session))))

(defroutes login-routes
  (GET "/login" [] login)
  (GET "/home" [] home)
  (POST "/login" [email password :as {session :session}] (login-authenticate email password session)))
