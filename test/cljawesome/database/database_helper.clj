(ns cljawesome.database.database-helper
  (:require [environ.core :refer [env]]
            [clojure.string :as str]
            [clojure.java.jdbc :as jdbc]))

;(defn pgdb []
  ;(let [url (java.net.URI. (env :database-url))
        ;params (str/split (.getUserInfo url ) #":")]
     ;(println "FEE " + (str/join "//" (.getHost url) (str/replace (.getPath url) #"/" "")))
    ;{ :subprotocol "postgresql"
     ;:subname (str/join "//" (.getHost url) (str/replace (.getPath url) #"/" ""))}))

(def pgdb
  { :subprotocol "postgresql"
    :subname "//localhost:5432/cljawesome" })

(defn players-by-season [seasonId]
  (first (jdbc/query pgdb ["select count(*) from seasons_persons where season_id = ?" seasonId] :row-fn :count)))

(defn player-game-stats [playerId]
  (first (jdbc/query pgdb ["select * from Persons_Games_Stats where person_id = ?" playerId] )))

(defn players-with-name [email]
  (first (jdbc/query pgdb ["select count(*) from persons where email = ?" email] :row-fn :count)))

(defn entries-in-teams-table [name]
  (first (jdbc/query pgdb ["select count(*) from teams where name = ?" name] :row-fn :count)))

(defn resetdb! []
(jdbc/execute! pgdb ["delete from admins;"] )
(jdbc/execute! pgdb ["delete from persons_games_stats;"] )
(jdbc/execute! pgdb ["delete from seasons_persons;"] )
(jdbc/execute! pgdb ["delete from persons;"] )
(jdbc/execute! pgdb ["delete from teams_leagues;"] )
(jdbc/execute! pgdb ["delete from teams_seasons;"] )
(jdbc/execute! pgdb ["delete from games;"] )
(jdbc/execute! pgdb ["delete from teams;"] )
(jdbc/execute! pgdb ["delete from seasons;"] )
(jdbc/execute! pgdb ["delete from leagues;"] ))
