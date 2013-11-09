(ns genmo.plot
  (:require [genmo.characters :as cs]
            [genmo.data.locations :as data.locations]))

(defn set-up-characters
  [details characters]
  (into details
        (for [[role properties] characters]
          [role (cs/create properties)])))

(defn set-up-locations
  [details locations]
  (into details
        (for [[detail properties] locations
              :let [location (rand-nth
                               (data.locations/satisfying (:tags properties)))]]
          [detail location])))

(defn set-up
  [{:keys [requirements]}]
  (-> {}
    (set-up-characters (:characters requirements))
    (set-up-locations (:locations requirements))))
