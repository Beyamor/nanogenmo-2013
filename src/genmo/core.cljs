(ns genmo.core
  (:use [jayq.core :only [$ text]])
  (:require [genmo.data.names :as names]))

(let [gender (rand-nth [:male :female])]
  (def story
    {:protagonist
     {:gender gender
      :name (rand-nth (names/all gender))}}))

($ #(-> ($ :body)
      (text (str story))))
