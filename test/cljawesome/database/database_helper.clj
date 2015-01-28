(ns cljawesome.database.database-helper
  (:require [clojure.java.jdbc :as jdbc]))

(def pgdb
  { :subprotocol "postgresql"
    :subname "//localhost:5432/cljawesome" })

(defn resetdb! []
  (jdbc/execute! pgdb ["delete from teams_leagues;"] )
  (jdbc/execute! pgdb ["delete from teams_seasons;"] )
  (jdbc/execute! pgdb ["delete from games;"] )
  (jdbc/execute! pgdb ["delete from teams;"] )
  (jdbc/execute! pgdb ["delete from divisions;"] )
  (jdbc/execute! pgdb ["delete from seasons;"] )
  (jdbc/execute! pgdb ["delete from leagues;"] ))
