(ns genmo.data.locations
  (:require [cljs.core.set :as s]))

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
              (= tags v)))
    (map first)))
