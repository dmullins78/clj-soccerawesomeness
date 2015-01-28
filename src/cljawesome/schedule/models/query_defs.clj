(ns cljawesome.schedule.models.query-defs
  (:require [environ.core :refer [env]]
            [yesql.core :refer [defqueries]]))


(defqueries "cljawesome/schedule/models/schedule_queries.sql" {:connection (env :database-url)})
