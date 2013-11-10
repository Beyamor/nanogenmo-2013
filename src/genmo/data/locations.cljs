(ns genmo.data.locations
  (:require [clojure.set :as s]))

(def all
  {"Town"
   #{:human}
   
   "Palace"
   #{:human :royal}

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
