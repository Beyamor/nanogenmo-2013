(ns genmo.plot
  (:require [genmo.characters :as cs]
            [genmo.data.locations :as data.locations]))

(defn set-up-characters
  [details characters]
  (into details
        (for [[role tags] characters]
          [role (cs/create tags)])))

(defmulti create-missing-detail (fn [_ [property _]] property))

(defmethod create-missing-detail :parent-of
  [details [_ child]]
  (let [tags (get-in details [child :tags])]
    (cs/create tags)))

(defmethod create-missing-detail :home-of
  [details [_ owner]]
  (let [tags (get-in details [owner :tags])]
    (rand-nth (data.locations/satisfying tags))))

(defn add-missing-details
  [details thing]
  (cond
    (string? thing)
    details

    (keyword? thing)
    details

    (vector? thing)
    (reduce add-missing-details details thing)

    (map? thing)
    (let [descriptor (first thing)]
      (if (contains? details descriptor)
        details
        (assoc details thing
               (create-missing-detail details descriptor))))))

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
