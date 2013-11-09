(ns genmo.characters
  (:require [genmo.data.names :as names]))

(def any-gender #(rand-nth [:male :female]))

(def types #{:human :monster})

(defmulti create-
  #(->> % (filter types) first))

(defmethod create- :human
  [tags]
  (let [gender (any-gender)]
    {:gender gender
     :name (rand-nth (names/all gender))}))

(defmethod create- :monster
  [tags]
  {:gender (any-gender)
   :name (rand-nth ["Dragon" "Minotaur" "Ogre" "Giant"])})

(defn create
  [{:keys [tags]}]
  (merge
    {:tags tags}
    (create- tags)))
