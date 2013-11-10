(ns genmo.data.locations
  (:require [clojure.set :as s]))

(def all
  {"Palace"
   #{:human}

   "Castle"
   #{:human :royal}

   "Cave"
   #{:monster}

   "Swamp"
   #{:monster}})

(defn satisfying
  [tags]
  (->> all
    (filter (fn [[_ v]]
              (s/superset? v tags)))
    (map first)))
