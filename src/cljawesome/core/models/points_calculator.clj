(ns cljawesome.core.models.points-calculator)

(defn- goaldiff [g]
  (- (:us g) (:them g)))

(defn- summary [points goal_diff]
  { :points points :diff goal_diff })

(defn- calculate_game [g]
  (let [diff (goaldiff g)]
    (cond
      (> diff 0) (summary 3 diff)
      (= diff 0) (summary 1 diff)
      :else (summary 0 diff))))

(defn- sum_value [results value]
  (->> results (map #(value %)) (reduce +)))

(defn- sum_differential [results]
  (sum_value results :diff))

(defn- sum_points [results]
  (sum_value results :points))

(defn calculate [games]
  (let [results (map #(calculate_game %) games)]
    [(sum_points results) (sum_differential results)]))
