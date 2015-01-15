(ns cljawesome.person.views.login-view
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.core :refer [html h]]))

(defn login-form []
  (html
    [:div.contact
      [:form {:action "/person/login" :method "post"}
        [:div.column-1
          [:input#name-input {:type "text" :name "email" :placeholder "Name"}]]
        [:div.column-2
          [:input#password-input {:type "password" :name "password" :placeholder "Password"}]]
        [:button.button.add {:type "submit"} "Login "]]
        [:div.clear-row]]))

(defn login-success []
  (html
    [:h1 "Success!"]))

