(ns genmo.core
  (:use [jayq.core :only [$ html]])
  (:require [genmo.story :as story]
            [genmo.write :as write]
            [genmo.present :as present]))

(set! *print-fn*
      (fn [& args]
        (->> args
          (map str)
          (interpose " ")
          (apply str)
          (.log js/console))))

($ #(->>
      (story/create)
      write/story
      present/story
      (html ($ :body))))
