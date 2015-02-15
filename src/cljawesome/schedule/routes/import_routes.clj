(ns cljawesome.schedule.routes.import-routes
  (:require [compojure.core :refer :all]
            (ring.middleware [multipart-params :as mp])
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [cljawesome.util.league-params :as lp]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.importer :as importer]
            [cljawesome.person.models.importer :as roster]
            [ring.util.response :refer [resource-response response]]))

(defn show-load-rosters [league year season request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (render-file "roster-import.html" { :base (lp/basepath league year season)}))

(defn load-rosters [league year season file request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league year season)
        games (roster/import-people (:id league) (:seasonid league) (file :tempfile))]
    (render-file "roster-import.html" { :base (lp/base-path league) :rosters true})))

(defn show-load-schedule [league year season request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (render-file "schedule-import.html" { :base (lp/basepath league year season)}))

(defn load-schedule [league year season file request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league year season)
        games (importer/import_schedule (:id league) (:seasonid league) (file :tempfile))]
    (render-file "schedule-import.html" { :base (lp/base-path league) :games games})))

(defroutes schedule-routes
  (GET "/:league/:year/:season/roster/load" [league year season :as request] (show-load-rosters league year season request))
  (mp/wrap-multipart-params
    (POST "/:league/:year/:season/roster/load" [league year season file :as request]
         (load-rosters league year season file request )))
  (GET "/:league/:year/:season/schedule/load" [league year season :as request] (show-load-schedule league year season request))
  (mp/wrap-multipart-params
    (POST "/:league/:year/:season/schedule/load" [league year season file :as request]
         (load-schedule league year season file request ))))
