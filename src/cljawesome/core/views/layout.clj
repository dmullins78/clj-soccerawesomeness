(ns cljawesome.core.views.layout
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.core :refer [html h]]))

(defn common-layout [& body]
  (html5
    [:head
     [:title "SoccerAwesomeness"]
     (include-css "/css/soccerawesomeness.css")
     (include-css "http://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css")]
    [:body
     [:div#wrapper
      [:h1#content-title "Soccer Awesomeness"]
        [:div.clear-row]]
       body]))

