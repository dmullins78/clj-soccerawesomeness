(ns cljawesome.league.models.normalizer)

(defn- is_home [id game]
  (= (:home_team_id game) id))

(defn- home_normalization [g]
  (println "EE " + g)
  (hash-map
    :us (:ht_score g)
    :them (:at_score g)
    :played (> (:update_count g) 0)))

(defn- away_normalization [g]
  (println "EE3 " + g)
  (hash-map
    :us (:at_score g)
    :them (:ht_score g)
    :played (> (:update_count g) 0)))

(defn- normalize-game [id g]
  (if-let [was_home (is_home id g)]
    (home_normalization g)
    (away_normalization g)))

(defn normalize [team-id games]
  (map #(normalize-game team-id %) games))

