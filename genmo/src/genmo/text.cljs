(ns genmo.text
  (:use [genmo.util :only [flatten-1]]))

(defn sen
  [& bits]
  (->> bits
    (cons :sentence)
    vec))

(defn sens
  [& sentences]
  (map (partial apply sen) sentences))

(defn par
  [& sentences]
  (->> sentences
    (cons :paragraph)
    vec))
