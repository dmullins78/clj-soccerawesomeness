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

(defn show-admin [league season adminId]
  (let [lg (league/get-season league season)
        admin (adb/get-league-admin {:adminId (Integer. adminId) :leagueId (:id lg)})
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admin (first admin) :admins admins :base (lp/basepath league season)})))

(defn show-admins [league season]
  (let [lg (league/get-season league season)
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins :base (lp/basepath league season)})))

(defn delete-admin [league season id request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (adb/delete-league-admin<! {:adminId (Integer. id)}) "")

(defn update-admin [league season id email password request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (adb/update-league-admin<! {:adminId (Integer. id) :email email :password password})
  (let [lg (league/get-season league season)
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins})))

(defn add-admin [league season email password request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [lg (league/get-season league season)
        admin (adb/insert-admin<! {:email email :password password :leagueId (:id lg) })
        admins (adb/get-league-admins {:leagueId (:id lg)})]
    (render-file "admin.html" {:admins admins})))

(defroutes admin-routes
  (GET "/:league/:season/admins" [league season] (show-admins league season))
  (GET "/:league/:season/admins/:adminId" [league season adminId :as request] (show-admin league season adminId))
  (DELETE "/:league/:season/admins/:adminId" [league season adminId :as request] (delete-admin league adminId request))
  (POST "/:league/:season/admins" [league season email password :as request] (add-admin league email password request))
  (POST "/:league/:season/admins/:adminId" [league season adminId email password :as request] (update-admin league season adminId email password request)))
