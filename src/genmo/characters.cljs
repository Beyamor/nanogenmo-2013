(ns genmo.characters
  (:require [genmo.data.names :as names]))

(defmulti create :type)

(defmethod create :human
  [constraints]
  (let [gender (rand-nth [:male :female])]
    {:gender gender
     :name (rand-nth (names/all gender))}))
