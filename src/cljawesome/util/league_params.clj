(ns cljawesome.util.league-params
  (:require [cljawesome.league.models.query-defs :as league]))

(defn parse-params [params]
  (league/get-season
    (get params :name)
    (get params :year)
    (get params :season)))

