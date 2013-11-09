(ns genmo.core
  (:use [jayq.core :only [$ text]])
  (:require [genmo.story :as story]))

($ #(-> ($ :body)
      (text (str (story/create)))))
