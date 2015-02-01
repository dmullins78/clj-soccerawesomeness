(ns cljawesome.util.filter)

(defn find-first
  [value allValues property]
  (first (filter #(= (property %) value) allValues)))
