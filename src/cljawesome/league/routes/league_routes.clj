(ns cljawesome.league.routes.league-routes
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [resource-response response]]
            [selmer.parser :refer [render-file]]
            [cljawesome.core.mailer :as email]
            [cljawesome.util.league-params :as lp]
            [cljawesome.players.models.query-defs :as player]
            [cljawesome.league.models.query-defs :as query]))

(defn get-league [league year season]
  (let [league (query/select-league-season {:name league :year year :season season})]
    (response (first league) )))

(defn get-divisions [team]
  (:division team))

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
        sorted-divisions (group-by :division sorted-teams)]
    (render-file "league.html" {:league lg :offenders top-offenders :scorers top-scorers :teams sorted-divisions :base (lp/basepath league season)})))

(defroutes league-routes
  (GET  "/poing/:text" [text] (email/summary text))
  (GET  "/:league/:season" [league season] (get-teams league season))
  (GET  "/:league/:season/performance" [league season division] (get-division league season division)))
