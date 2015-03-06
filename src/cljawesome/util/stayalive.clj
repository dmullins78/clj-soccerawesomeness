(ns cljawesome.util.stayalive
  (:require  [clj-http.client :as http]))

(defn http-get[address]
  (try ((http/get address {:throw-exceptions false}) :status)
       (catch Exception e -1)))

(defn website-up?[address]
  (let [status (http-get address) ]
    (cond 
      (>= status 500) "INTERNAL SERVER ERROR"
      (>= status 400) "CLIENT ERROR, probably not good."
      (>= status 300) "REDIRECTED, ok maybe?"
      (>= status 200) "OK"
      (== status -1) "Exception. Check the url and protocol?"
      :else "Unknown response")))

(defn ping []
  (website-up? "http://morning-bayou-6001.herokuapp.com/login"))
