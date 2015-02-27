(ns cljawesome.core.mailer
  (:require [environ.core :refer [env]]
            [postal.core :refer :all]))

(defn summary [pong]
  (send-message {:host "smtp.sendgrid.net"
                 :user (env :mail-user)
                 :pass (env :mail-password)}
                {:from "dmullins78@gmail.com"
                 :to "dmullins78@gmail.com"
                 :subject (str "Hi " pong)
                 :body "Test."})
  (str "Got it - " pong))
