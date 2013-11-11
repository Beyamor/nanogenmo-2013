(ns genmo.data.locations
  (:require [clojure.set :as s]))

(def all
  {"town"
   {:home-to #{:human}}
   
   "palace"
   {:home-to #{:human :royal}}

   "castle"
   {:home-to #{:human :royal}}

   "cave"
   {:home-to #{:monster}}

   "swamp"
   {:home-to #{:monster}}})

(defn home-to
  [tags]
  (->> all
    (filter (fn [[_ {:keys [home-to]}]]
              (s/superset? home-to tags)))
    (map first)))
