(ns cljawesome.league.models.points-calculator)

(defn- goaldiff [g]
  (- (:us g) (:them g)))

(defn- summary [points goal_diff game win loss tie]
  {:points points
   :diff goal_diff
   :scored (:us game)
   :allowed (:them game)
   :win win
   :loss loss
   :tie tie})

(defn- calculate_game [g]
  (let [diff (goaldiff g)]
    (cond
      (> diff 0) (summary 3 diff g 1 0 0)
      (and
        (or
          (> (:us g) 0)
          (> (:them g) 0)) (= diff 0)) (summary 1 diff g 0 0 1)
      (< diff 0) (summary 0 diff g 0 1 0)
      :else (summary 0 diff g 0 0 0))))

(defn- sum_value [results value]
  (->> results (map #(value %)) (reduce +)))

(defn calculate [games]
  (let [results (map #(calculate_game %) games)]
    [(sum_value results :points)
     (sum_value results :diff)
     (sum_value results :scored)
     (sum_value results :allowed)
     (sum_value results :win)
     (sum_value results :loss)
     (sum_value results :tie)]))
