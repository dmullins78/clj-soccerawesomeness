(ns cljawesome.admin.models.query-defs
  (:require [environ.core :refer [env]]
            [yesql.core :refer [defqueries]]))

(defqueries "cljawesome/admin/models/queries.sql" {:connection (env :database-url)})
