(ns cljawesome.schedule.routes.import-routes
  (:require [compojure.core :refer :all]
            (ring.middleware [multipart-params :as mp])
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [cljawesome.util.league-params :as lp]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.schedule.models.query-defs :as sdb]
            [cljawesome.schedule.models.importer :as importer]
            [cljawesome.players.models.importer :as roster]
            [ring.util.response :refer [resource-response response]]))

(defn get-league-team-aliases [league season]
  (let [league (league/get-season league season)]
    (sdb/team-import-aliases { :leagueId (:id league) })))

(defn delete-alias [teamId]
  (sdb/delete-alias<! { :teamId (Integer. teamId)}))

(defn add-league-team-alias [league body]
  (sdb/insert-league-alias<!
    { :leagueId (:id league)
     :teamId (Integer. (:id body))
     :teamAlias (:alias body)}))

(defn show-load-rosters [league season request]
  ;(when (not (authenticated? request))
  ;(throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league season)
        teams (sdb/teams-by-season { :seasonId (:seasonid league) })]
    (render-file "roster-import.html" { :teams teams :base (lp/basepath league season)})))

(defn load-rosters [league season file request]
  (when (not (authenticated? request))
    (throw-unauthorized {:message "Not authorized"}))
  (let [league (league/get-season league season)
        games (roster/import-players (:id league) (:seasonid league) (file :tempfile))]
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
  (GET "/:league/:season/roster/load/aliases" [league season] (get-league-team-aliases league season))
  (DELETE "/:name/:season/roster/load/aliases/:aliasId" [aliasId] (delete-alias aliasId))
  (PUT "/:name/:season/roster/load/aliases/:id" {params :params body :body}
        (add-league-team-alias (lp/parse-params params) body))

  (mp/wrap-multipart-params
    (POST "/:league/:season/roster/load" [league season file :as request]
          (load-rosters league season file request )))
  (GET "/:league/:season/schedule/load" [league season :as request] (show-load-schedule league season request))
  (mp/wrap-multipart-params
    (POST "/:league/:season/schedule/load" [league season file :as request]
          (load-schedule league season file request ))))
