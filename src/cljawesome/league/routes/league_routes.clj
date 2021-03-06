(ns cljawesome.league.routes.league-routes
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [resource-response response]]
            [selmer.parser :refer [render-file]]
            [cljawesome.core.mailer :as email]
            [cljawesome.util.league-params :as lp]
            [cljawesome.players.models.query-defs :as player]
            [cljawesome.league.models.query-defs :as query]))

(defn get-league [league season]
  (let [league (query/select-league-season {:name league :season season})]
    (first league) ))

(defn get-divisions [team]
  (:division team))

(defn division-hierarchy [d1 d2]
  (compare (first d1) (first d2)))

(defn soccer-scorer
  [t1 t2]
  (cond
    (> (:points t1) (:points t2)) true
    (< (:points t1) (:points t2)) false
    :else
    (cond
      (> (:goal_differential t1) (:goal_differential t2)) true
      (< (:goal_differential t1) (:goal_differential t2)) false
      :else
      (> (:scored t1) (:scored t2)))))

(defn get-division [league season division]
  (let [lg (first (query/select-league-by-name {:path league :season season}))
        top-scorers (player/top-scorers-by-division {:division division :seasonId (:seasonid lg)})
        top-offenders (player/top-offenders-by-division {:division division :seasonId (:seasonid lg)})]
    (render-file "division-standings.html" {:division division :league lg :offenders top-offenders :scorers top-scorers :base (lp/basepath league season)})))

(defn get-teams [league season]
  (let [teams (query/get-team-standings league season)
        lg (first (query/select-league-by-name {:path league :season season}))
        top-scorers (player/top-scorers {:seasonId (:seasonid lg)})
        top-offenders (player/top-offenders {:seasonId (:seasonid lg)})
        sorted-teams (sort (comp soccer-scorer) teams)
        sorted-divisions (group-by :division sorted-teams)
        sorted-divisions (sort division-hierarchy sorted-divisions)]
    (render-file "league.html" {:league lg :offenders top-offenders :scorers top-scorers :teams sorted-divisions :base (lp/basepath league season)})))

(defn get-teams-json [league season]
  (let [lg (get-league league season)]
    (query/select-season-teams { :seasonId (:seasonid lg)})))

(defroutes league-routes
  (GET  "/:league/:season/summary" [league season] (email/summary league season))
  (GET  "/:league/:season/api/teams" [league season] (get-teams-json league season))
  (GET  "/:league/:season" [league season] (get-teams league season))
  (GET  "/:league/:season/performance" [league season division] (get-division league season division)))
