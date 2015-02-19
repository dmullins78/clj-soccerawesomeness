(ns cljawesome.migrations.migration
  (import [org.flywaydb.core Flyway])
  (require [clojure.java.jdbc :refer [db-do-commands]]
           [clojure.string :as str]
           [environ.core :refer [env]]))


(defn get-db-datasource []
  (let [url (java.net.URI. (env :database-url))
        params (str/split (.getUserInfo url ) #":")]
  (doto (org.postgresql.ds.PGSimpleDataSource.)
    (.setServerName (.getHost url))
    (.setDatabaseName (str/replace (.getPath url) #"/" ""))
    (.setUser (first params))
    (.setPassword (second params)))))

(defn migrate []
  (let [datasource (get-db-datasource)
        flyway (doto (Flyway.)
                 (.setDataSource datasource)
                 (.setSqlMigrationPrefix ""))]
    (.migrate flyway)))
