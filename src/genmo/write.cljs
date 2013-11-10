(ns genmo.write
  (:require [genmo.lang :as lang]))

(defn realize
  [thing details]
  (cond
    (string? thing)
    thing

    (keyword? thing)
    (lang/refer-to (get details thing))

    (sequential? thing)
    (-> thing
      (->>
        (map #(realize % details))
        (interpose " ")
        (apply str))
      (str "\n"))

    (set? thing)
    (-> thing vec rand-nth))) 

(defn story
  [{:keys [details events]}]
  (for [{:keys [name description]} events]
    {:section
     name

     :content
     (realize description details)}))
