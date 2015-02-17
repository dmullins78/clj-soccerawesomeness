(ns cljawesome.admin.routes.admin-routes
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [cljawesome.league.models.query-defs :as league]
            [compojure.response :refer [render]]
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [cljawesome.person.models.query-defs :as pdb]
            [cljawesome.admin.models.query-defs :as adb]
            [cljawesome.util.league-params :as lp]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn show-admins [league year season request]
  ;(when (not (authenticated? request))
  ;(throw-unauthorized {:message "Not authorized"}))
  (let [lg (league/get-season league year season)
        teams (league/select-teams-by-season {:seasonId (:seasonid lg)})]
    (render-file "admin.html" {:teams teams :base (lp/basepath league year season)})))

(defn add-admin [league year season email teams request]
  ;(when (not (authenticated? request))
  ;(throw-unauthorized {:message "Not authorized"}))
  (let [lg (league/get-season league year season)
        admin (adb/insert-admin<! {:email email :season (:seasonid lg) })]
    (doseq [team teams]
        (adb/insert-admin-teams<! {:adminId (:id admin) :teamId team :role "teamadmin"}))
    (render-file "admin.html" {}))

  (defroutes admin-routes
    (GET "/:league/:year/:season/admin" [league year season :as request] (show-admins league year season request))
    (POST "/:league/:year/:season/admin" [league year season email teams :as request] (add-admin league year season email teams request)))
