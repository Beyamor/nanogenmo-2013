(ns genmo.plot
  (:use [genmo.util :only [insertv]]
        [genmo.text :only [sen par]])
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

(defmulti requirement-satisfied?
  (fn [requirement index-of-event events]
    (:condition requirement)))

(defmulti satisfy-requirement
  (fn [requirement event]
    (:condition requirement)))

(defmethod requirement-satisfied? :at-location
  [requirement index-of-event events]
  (if (zero? index-of-event)
    true
    ; ugh, this assumes location is based directly on previous event
    ; but, like, the previous event could leave the location unchanged, right?
    ; so, yeesh, TODO scan backwards through all events until location is established
    (let [curr          (nth events index-of-event)
          curr-location  (-> curr :setting :location)
          prev          (nth events (dec index-of-event))
          prev-location  (-> prev :setting :location)]
      (or
        (= curr-location prev-location)
        (= curr-location (:resulting-location prev))))))

(defmethod satisfy-requirement :at-location
  [{:keys [who]} {{:keys [location]} :setting}]
  (let [people? (not (keyword? who))]
    {:name              "Travel"
     :description       [(par
                           (sen
                             (if people? (interpose " and " who) who)
                             (if people? "travel to" "travels to")
                             location))]
     :resulting-location location}))

(defn event-requirement-state
  "Either returns :satisfied-all-requirements or the index of the unsatisfied requirement."
  [{requirements :requires :as event} event-index events]
  (loop [requirement-index 0]
    (if (or (not requirements)
            (>= requirement-index (count requirements)))
      :satisfied-all-requirements
      (if (requirement-satisfied?
            (nth requirements requirement-index)
            event-index events)
        (recur (inc requirement-index))
        requirement-index))))

(defn satisfy-requirements
  [events]
  (loop [events events, event-index 0]
    (if (>= event-index (count events))
      events
      (let [event (nth events event-index)
            satisfaction (event-requirement-state event event-index events)]
        (if (= satisfaction :satisfied-all-requirements)
          (recur events (inc event-index))
          (let [satisfying-event (satisfy-requirement
                                    (-> event :requires (nth satisfaction))
                                    event)]
            (recur (insertv events event-index satisfying-event) 0)))))))

(defn set-up
  [{:keys [characters events]}]
  (-> {:details {}
       :events events}
    (update-in [:details] set-up-characters characters)
    (update-in [:details] add-details-for-events events)
    (update-in [:events] satisfy-requirements)))
