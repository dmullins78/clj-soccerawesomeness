(ns cljawesome.login.routes.login-routes
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [compojure.response :refer [render]]
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [cljawesome.person.models.query-defs :as pdb]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn login [request]
  (render-file "login.html" {}))

(defn home [request]
  (let [session (:session request)]
    (render-file "home.html" {:person (:identity session)})))

(defn auth [email]
  (first (pdb/admin-roles {:email email})))

(defn login-authenticate
  [email password session]
  (let [session (assoc session :identity (auth email))]
    (-> (redirect "/home")
        (assoc :session session))))

(defroutes login-routes
  (GET "/login" [] login)
  (GET "/home" [] home)
  (POST "/login" [email password :as {session :session}] (login-authenticate email password session)))
