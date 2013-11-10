(ns genmo.plot
  (:require [genmo.characters :as cs]
            [genmo.data.locations :as data.locations]))

(defn set-up-characters
  [details characters]
  (into details
        (for [[role tags] characters]
          [role (cs/create tags)])))

(defmulti create-missing-detail (fn [derivation _] derivation))

(defmethod create-missing-detail :parent-of
  [_ child]
  (cs/create (:tags child)))

(defmethod create-missing-detail :home-of
  [_ owner]
  (->> (:tags owner)
    data.locations/home-to
    rand-nth))

(defn add-derived-detail
  [details detail]
  (let [detail-pieces (-> detail
                        name
                        (clojure.string/split #"-"))
        source (->> detail-pieces last keyword details)
        derivation (->> detail-pieces butlast (interpose "-") (apply str) keyword)]
    (assoc details detail
           (create-missing-detail derivation source))))

(defn add-missing-details
  [details thing]
  (cond
    (string? thing)
    details

    (keyword? thing)
    (if (contains? details thing)
      details
      (add-derived-detail details thing))

    (sequential? thing)
    (reduce add-missing-details details thing)

    (set? thing)
    details))

(defn add-character-details
  [details characters]
  (reduce add-missing-details
          details characters))

(defn add-setting-details
  [details setting]
  (reduce add-missing-details
          details (map setting [:location])))

(defn add-event-details
  [details {:as event :keys [characters setting]}]
  (-> details
    (add-setting-details setting)
    (add-character-details characters)))

(defn add-details-for-events
  [details events]
  (let [events (reverse events)]
    (reduce add-event-details details events)))

(defn set-up
  [{:keys [characters events]}]
  (-> {}
    (set-up-characters characters)
    (add-details-for-events events)))
