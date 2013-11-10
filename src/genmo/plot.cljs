(ns genmo.plot
  (:require [genmo.characters :as cs]
            [genmo.data.locations :as data.locations]))

(defn set-up-characters
  [details characters]
  (into details
        (for [[role properties] characters]
          [role (cs/create properties)])))

(defn location-constraints
  [details properties]
  (->>
    (for [[property value] properties]
      (case property
        :tags
        value

        :home-of
        (let [character (get details value)]
          (:tags character))))
    (apply concat)
    set))

(defn set-up-locations
  [details locations]
  (into details
        (for [[detail properties] locations
              :let [location (->
                               (location-constraints details properties)
                               data.locations/satisfying
                               rand-nth)]]
          [detail location])))

(defn set-up
  [{:keys [requirements]}]
  (-> {}
    (set-up-characters (:characters requirements))
    (set-up-locations (:locations requirements))))
