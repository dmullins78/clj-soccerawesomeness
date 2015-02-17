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

(defn show-admin [league year season adminId]
  (let [lg (league/get-season league year season)
        admin (adb/get-league-admin {:adminId (Integer. adminId) :leagueId (:id lg)})
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admin (first admin) :admins admins :base (lp/basepath league year season)})))

(defn show-admins [league year season]
  (let [lg (league/get-season league year season)
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins :base (lp/basepath league year season)})))

(defn update-admin [league year season id email password request]
  ;(when (not (authenticated? request))
  ;(throw-unauthorized {:message "Not authorized"}))
  (adb/update-league-admin<! {:adminId (Integer. id) :email email :password password})
  (let [lg (league/get-season league year season)
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins})))

(defn add-admin [league year season email password request]
  ;(when (not (authenticated? request))
  ;(throw-unauthorized {:message "Not authorized"}))
  (let [lg (league/get-season league year season)
        admin (adb/insert-admin<! {:email email :password password :leagueId (:id lg) })
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins})))

(defroutes admin-routes
  (GET "/:league/:year/:season/admins" [league year season] (show-admins league year season))
  (GET "/:league/:year/:season/admins/:adminId" [league year season adminId :as request] (show-admin league year season adminId))
  (POST "/:league/:year/:season/admins" [league year season email password :as request] (add-admin league year season email password request))
  (POST "/:league/:year/:season/admins/:adminId" [league year season adminId email password :as request] (update-admin league year season adminId email password request)))
