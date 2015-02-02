(ns cljawesome.person.models.query-defs
  (:require [environ.core :refer [env]]
            [yesql.core :refer [defqueries]]))

(defqueries "cljawesome/person/models/queries.sql" {:connection (env :database-url)})
