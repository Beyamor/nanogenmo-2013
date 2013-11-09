(ns genmo.characters
  (:require [genmo.data.names :as names]))

(def any-gender #(rand-nth [:male :female]))

(defmulti create :type)

(defmethod create :human
  [constraints]
  (let [gender (any-gender)]
    {:gender gender
     :name (rand-nth (names/all gender))}))

(defmethod create :monster
  [constraints]
  {:gender (any-gender)
   :name (rand-nth ["Dragon" "Minotaur" "Ogre" "Giant"])})
