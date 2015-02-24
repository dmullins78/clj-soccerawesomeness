(ns cljawesome.players.models.query-defs
  (:require [environ.core :refer [env]]
            [yesql.core :refer [defqueries]]))

(defqueries "cljawesome/players/models/queries.sql" {:connection (env :database-url)})
