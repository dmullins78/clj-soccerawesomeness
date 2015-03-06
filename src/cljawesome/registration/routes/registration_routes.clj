(ns cljawesome.registration.routes.registration-routes
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [cljawesome.core.mailer :refer [interest]]
            [clojure.java.io :as io]))

(defn register [email]
  (interest email)
  (io/resource "public/index-success.html"))

(defroutes registration-routes
  (POST "/register" [email] (register email)))
