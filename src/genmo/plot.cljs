(ns genmo.plot
  (:require [genmo.characters :as cs]))

(defn set-up-characters
  [details characters]
  (into details
        (for [[role constraints] characters]
          [role (cs/create constraints)])))

(defn set-up-locations
  [details locations]
  (into details
        (for [[location options] locations]
          [location (rand-nth options)])))

(defn set-up
  [{:keys [requirements]}]
  (-> {}
    (set-up-characters (:characters requirements))
    (set-up-locations (:locations requirements))))
