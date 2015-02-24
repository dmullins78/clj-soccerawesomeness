(ns cljawesome.database.database-helper
  (:require [environ.core :refer [env]]
            [clojure.string :as str]
            [clojure.java.jdbc :as jdbc]))
(def pgdb
  { :subprotocol "postgresql"
    :subname "//localhost:5432/cljawesome-test" })

(defn players-by-season [seasonId]
  (first (jdbc/query pgdb ["select count(*) from seasons_players where season_id = ?" seasonId] :row-fn :count)))

(defn player-game-stats [playerId]
  (first (jdbc/query pgdb ["select * from Players_Games_Stats where player_id = ?" playerId] )))

(defn players-with-name [email]
  (first (jdbc/query pgdb ["select count(*) from players where email = ?" email] :row-fn :count)))

(defn entries-in-teams-table [name]
  (first (jdbc/query pgdb ["select count(*) from teams where name = ?" name] :row-fn :count)))

(defn resetdb! []
(jdbc/execute! pgdb ["delete from admins;"] )
(jdbc/execute! pgdb ["delete from players_games_stats;"] )
(jdbc/execute! pgdb ["delete from seasons_players;"] )
(jdbc/execute! pgdb ["delete from players;"] )
(jdbc/execute! pgdb ["delete from teams_leagues;"] )
(jdbc/execute! pgdb ["delete from teams_seasons;"] )
(jdbc/execute! pgdb ["delete from games;"] )
(jdbc/execute! pgdb ["delete from teams;"] )
(jdbc/execute! pgdb ["delete from seasons;"] )
(jdbc/execute! pgdb ["delete from leagues;"] ))
