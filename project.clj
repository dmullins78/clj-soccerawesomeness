(defproject cljawesome "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-json "0.2.0"]
                 [ring/ring-defaults "0.1.2"]
                 [postgresql/postgresql "9.1-901-1.jdbc4"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [yesql "0.5.0-rc1"]
                 [environ "1.0.0"]
                 [selmer "0.8.0"]
                 [clj-time "0.9.0"]
                 [buddy/buddy-core "0.3.0"]
                 [buddy/buddy-auth "0.3.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.flywaydb/flyway-core "3.0"]]
  :plugins [[lein-ring "0.8.13"]
            [lein-midje "3.1.3"]
            [lein-environ "1.0.0"]]
  :ring {:handler cljawesome.core.handler/app
         :init cljawesome.core.handler/init }
  :aliases {"migrate" ["run" "-m" "cljawesome.migrations.migration/migrate"]}
  :profiles {:test-local {:dependencies [[midje "1.6.3"]
                                         [javax.servlet/servlet-api "2.5"]
                                         [ring-mock "0.1.5"]]

                          :plugins     [[lein-midje "3.1.3"]]}

             ;; Set these in ./profiles.clj
             :test-env-vars {}
             :dev-env-vars  {}

             :test [:test-local :test-env-vars]
             :dev  {:test-paths ^:replace []}
             :production {:ring {:open-browser? false
                                 :stacktraces?  false
                                 :auto-reload?  false}}})
