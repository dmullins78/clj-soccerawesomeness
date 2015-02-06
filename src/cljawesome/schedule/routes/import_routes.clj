(ns cljawesome.schedule.routes.import-routes
  (:require [compojure.core :refer :all]
            (ring.middleware [multipart-params :as mp])
            [clojure.java.io :as io]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.importer :as importer]
            [ring.util.response :refer [resource-response response]]))

(defn parse-params [params]
  [(get params :file)
   (league/get-season
     (get params :name)
     (get params :year)
     (get params :season))
   ])

(defn load-schedule [[file league]]
  (importer/import_schedule (:id league) (:seasonid league) (file :tempfile))
  "Success")

(defroutes schedule-routes
  (mp/wrap-multipart-params
    (POST "/league/:name/:year/:season/schedule/load" {params :params} (load-schedule (parse-params params) ))))
