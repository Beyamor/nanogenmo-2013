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

    (vector? thing)
    (reduce add-missing-details details thing)))

(defn add-all-missing-details
  [details sections]
  (reduce
    (fn [details [section definition]]
      (add-missing-details details definition))
    details sections))

(defn set-up
  [{:keys [requirements sections]}]
  (-> {}
    (set-up-characters (:characters requirements))
    (add-all-missing-details sections)))
