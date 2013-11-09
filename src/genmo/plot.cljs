(ns genmo.plot
  (:require [genmo.characters :as cs]))

(defn set-up
  [{requirements :requires}]
  (into {}
        (for [character (:characters requirements)
              :let [[role constraints] (if (keyword? character)
                                         [character #{}]
                                         character)]]
          [role (cs/create constraints)])))
