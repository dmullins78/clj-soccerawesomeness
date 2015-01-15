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
                 [hiccup "1.0.5"]
                 [yesql "0.5.0-beta2"]
                 [org.flywaydb/flyway-core "3.0"]]
  :plugins [[lein-ring "0.8.13"]
            [lein-midje "3.1.3"]]
  :ring {:handler cljawesome.core.handler/app}
  :aliases {"migrate" ["run" "-m" "cljawesome.migrations.migration/migrate"]}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]
                        [midje "1.6.3"] ]}})
