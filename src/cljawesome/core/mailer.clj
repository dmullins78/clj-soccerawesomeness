(ns cljawesome.core.mailer
  (:require [environ.core :refer [env]]
            [cljawesome.league.models.query-defs :as league]
            [cljawesome.util.league-params :as lp]
            [selmer.parser :refer [render-file]]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [postal.core :refer :all]))
          
(def custom-formatter (f/formatter "MM/dd/yyyy"))

(defn summary-date-inteval []
  (let [end (t/now)
        start (t/minus (t/now) (t/weeks 1))
  (f/unparse custom-formatter start) (f/unparse custom-formatter end)))

(defn player-stats [game]
  (if-let [players (league/get-offenders-for-game {:gameId (:id game)})]
   [game players]
  [game []]))

(defn summary [lg season]
  (let [league (league/get-season lg season)
        games (league/select-recently-updated-games { :seasonId (:seasonid league)})
        game-players (map player-stats games)
        date-range (summary-date-inteval)
        text (render-file "emails/summary.txt" {:range date-range :league league :games game-players})]
    (send-message {:host "smtp.sendgrid.net"
                   :user (env :mail-user)
                   :pass (env :mail-password)}
                  {:from "dmullins78@gmail.com"
                   :to "dmullins78@gmail.com"
                   :subject (str "Weekly Summary")
                   :body text})
  text ))
