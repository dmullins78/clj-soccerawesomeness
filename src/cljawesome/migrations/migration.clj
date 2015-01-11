(ns cljawesome.migrations.migration
  (import [org.flywaydb.core Flyway])
  (require [clojure.java.jdbc :refer [db-do-commands]]
    [cljawesome.core.models.database :refer :all]))

;(def test-db (cljawesome.core.models.database/db))

(defn get-db-datasource []
  (doto (org.postgresql.ds.PGSimpleDataSource.)
    (.setServerName "localhost")
    (.setDatabaseName "cljawesome")
    (.setUser "clj2")
    (.setPassword "password")))

(defn migrate []
  (print "migrating 2 "))
  (let [datasource (get-db-datasource)
        flyway (doto (Flyway.)
                 (.setDataSource datasource)
                 (.setSqlMigrationPrefix ""))]
    (.migrate flyway))
