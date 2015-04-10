(ns cljawesome.admin.models.soccer-authentication
  (:require [environ.core :refer [env]]
            [yesql.core :refer [defqueries]]))

(defn notadmin? [user]
  (nil? user))

(defn teamadmin? [user]
    (println "TA " + (= "teamadmin" (:role user)))
    (= "teamadmin" (:role user)))

(defn leagueadmin? [user]
    (= "leagueadmin" (:role user)))

