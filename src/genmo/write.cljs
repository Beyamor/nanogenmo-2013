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
  [{:keys [plot details]}]
  (->>
    [:exposition :rising-action :climax :falling-action :denoument]
    (map (fn [section]
           {:section
            section

            :content
            (-> plot
              (get section)
              (realize details))}))))
