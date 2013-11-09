(ns genmo.story
  (:require [genmo.data.names :as names]))

(defn add-plot-point
  [{:keys [protagonist] :as story} section description]
  (-> story
    (assoc-in [:plot section] (description (:name protagonist)))))

(defn create-protagonist
  []
  (let [gender (rand-nth [:male :female])]
    {:gender gender
     :name (rand-nth (names/all gender))}))

(defn add-exposition
  [story]
  (-> story
    (add-plot-point :exposition
              #(str "Once upon a time, in a land far away, " % " did a thing."))))

(defn add-rising-action
  [story]
  (-> story
    (add-plot-point :rising-action
              #(str "Yes, surely " % " struggled against some conflict."))))

(defn add-climax
  [story]
  (-> story
    (add-plot-point :climax
              #(str "Having overcome great the hardship, " % " prepared for the final conflict."))))

(defn add-falling-action
  [story]
  (-> story
    (add-plot-point :falling-action
                    #(str "Great. " % " overcame that challenge or whatever."))))

(defn add-denoument
  [story]
  (-> story
    (add-plot-point :denoument
                    #(str % " lived happily every after."))))

(defn create
  []
  (-> {:plot {}}
    (assoc :protagonist (create-protagonist))
    add-denoument
    add-falling-action
    add-climax
    add-rising-action
    add-exposition))
