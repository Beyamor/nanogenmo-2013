(ns genmo.write
  (:use [genmo.util :only [flatten-1]])
  (:require [genmo.lang :as lang]))

(defn flatten-sentences
  [sentences]
  (->>
    (for [s sentences]
      (if (seq? s)
        (flatten-sentences s)
        [s]))
    flatten-1))

(defn realize
  [thing details]
  (cond
    (string? thing)
    thing

    (keyword? thing)
    (lang/refer-to (get details thing))

    (seq? thing)
    (->> thing
      (map #(realize % details))
      (apply str))

    (vector? thing)
    (let [[kind & content] thing]
      (case kind
        :sentence
        (->> content
          (filter identity)
          (map #(realize % details))
          (interpose " ")
          (apply str))

        :paragraph
        (-> content
          (->>
            flatten-sentences
            (map #(realize % details))
            (interpose " ")
            (apply str))
          (str "\n"))))

    (set? thing)
    (-> thing vec rand-nth))) 

(defn story
  [{:keys [details events]}]
  (for [{:keys [name description]} events]
    {:section
     name

     :content
     (realize (list* description) details)}))
