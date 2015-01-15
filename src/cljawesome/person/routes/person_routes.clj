(ns cljawesome.person.routes.person-routes
  (:require [compojure.core :refer :all]
            [cljawesome.core.views.layout :refer :all]
            [cljawesome.person.views.login-view :refer :all]
            [ring.util.response :refer [resource-response response]]))

(defn show-login [request]
  (common-layout
    (login-form)))

(defn do-login [request]
  (common-layout
    (login-success)))

(defroutes person-routes
  (GET  "/person/login" [] show-login)
  (POST  "/person/login" [] do-login))
