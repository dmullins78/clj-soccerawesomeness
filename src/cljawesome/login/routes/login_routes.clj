(ns cljawesome.login.routes.login-routes
  (:require [compojure.route :as route]
            [compojure.core :refer :all]
            [compojure.response :refer [render]]
            [selmer.parser :refer [render-file]]
            [clojure.java.io :as io]
            [ring.util.response :refer [response redirect content-type]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.params :refer [wrap-params]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [cljawesome.players.models.query-defs :as pdb]
            [cljawesome.util.league-params :as lp]
            [cljawesome.league.models.query-defs :as query]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn login [request]
  (render-file "login.html" {}))

(defn home [league season session]
  (let [player (:identity session)
        basepath (lp/basepath league season)
        lg (first (query/select-league-by-name {:path league :season season}))
        leaguepath (clojure.string/replace basepath #"/$" "")]
    (if (= "leagueadmin" (:role player))
      (render-file "home.html" {:base basepath :league lg :leaguepath leaguepath :admin true})
      (redirect (lp/basepath-lead-slash league season "teams")))))

(defn auth [email password]
  (when-let [credential (first (pdb/admin-roles {:email email :password password}))]
    (let [active-season (pdb/active-season {:leagueId (:leagueid credential)})]
      (assoc credential :active-season (first active-season)))))

(defn base-path [identity path]
  (let [season (:active-season identity)]
    (clojure.string/join "/" [(:league season) (:season season) path])))

(defn login-authenticate [email password session]
  (if-let [identity (auth email password)]
    (-> (redirect (base-path identity "home"))
        (assoc :session (assoc session :identity identity)))
    (render-file "login.html" {:error "Incorrect authentication credentials" })))

(defroutes login-routes
  (GET "/login" [] login)
  (GET "/:league/:season/home" [league season :as {session :session}] (home league season session))
  (POST "/login" [email password :as {session :session}] (login-authenticate email password session)))
