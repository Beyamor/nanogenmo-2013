(ns genmo.core
  (:use [jayq.core :only [$ html]])
  (:require [genmo.story :as story]
            [genmo.write :as write]
            [genmo.present :as present]))

($ #(->>
      (story/create)
      write/story
      present/story
      (html ($ :body))))
