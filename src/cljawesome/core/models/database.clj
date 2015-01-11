(ns cljawesome.core.models.database)

(def database {:test {:subprotocol "postgresql"
                      :subname "//127.0.0.1:5432/cljawesome"
                      :user "clj2"
                      :password "password"}})

(def db (database :test))
