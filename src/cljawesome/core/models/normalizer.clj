(ns cljawesome.core.models.normalizer)

(defn- is_home [id game]
  (= (:home_team_id game) id))

(defn- home_normalization [g]
  (hash-map
    :us (:ht_score g)
    :them (:at_score g)))

(defn- away_normalization [g]
  (hash-map
    :us (:at_score g)
    :them (:ht_score g)))

(defn- normalize-game [id g]
  (if-let [was_home (is_home id g)]
    (home_normalization g)
    (away_normalization g)))

(defn normalize [team-id games]
  (map #(normalize-game team-id %) games))

