(ns genmo.characters
  (:require [genmo.data.names :as names]))

(defn create-human
  [constraints]
  (let [gender (rand-nth [:male :female])]
    {:gender gender
     :name (rand-nth (names/all gender))}))

(def type-constructors
  {:human create-human})

(defn create
  [constraints]
  (if-let [constructor (-> constraints :type type-constructors)]
    (constructor constraints)
    (throw (js/Error. (str "No type constructor matches " (:type constraints))))))
