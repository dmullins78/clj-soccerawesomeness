(ns cljawesome.util.league-params
  (:require [cljawesome.league.models.query-defs :as league]
            [clojure.string :refer [lower-case]]))

(defn basepath [league season & others]
  (clojure.string/join "/" [league season (apply str others)]))

(defn base-path [league]
  (lower-case (format "%s/%s" (:league league)(:season league))))

(defn basepath-lead-slash [league season & others]
  (str "/" (clojure.string/join "/" [league season (apply str others)])))

(defn parse-params [params]
  (league/get-season
    (get params :name)
    (get params :season)))

