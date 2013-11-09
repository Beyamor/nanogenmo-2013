(ns genmo.story
  (:require [genmo.data.names :as names]))

(defn create-protagonist
  []
  (let [gender (rand-nth [:male :female])]
    {:gender gender
     :name (rand-nth (names/all gender))}))

(defn create
  []
  (-> {}
    (assoc :protagonist (create-protagonist))))
