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

(defn show-load-rosters [league season request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (render-file "roster-import.html" { :base (lp/basepath league season)}))

(defn load-rosters [league season file request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league season)
        games (roster/import-people (:id league) (:seasonid league) (file :tempfile))]
    (render-file "roster-import.html" { :base (lp/base-path league) :rosters true})))

(defn show-load-schedule [league season request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (render-file "schedule-import.html" { :base (lp/basepath league season)}))

(defn load-schedule [league season file request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league season)
        games (importer/import_schedule (:id league) (:seasonid league) (file :tempfile))]
    (render-file "schedule-import.html" { :base (lp/base-path league) :games games})))

(defroutes schedule-routes
  (GET "/:league/:season/roster/load" [league season :as request] (show-load-rosters league season request))
  (mp/wrap-multipart-params
    (POST "/:league/:season/roster/load" [league season file :as request]
         (load-rosters league season file request )))
  (GET "/:league/:season/schedule/load" [league season :as request] (show-load-schedule league season request))
  (mp/wrap-multipart-params
    (POST "/:league/:season/schedule/load" [league season file :as request]
         (load-schedule league season file request ))))
