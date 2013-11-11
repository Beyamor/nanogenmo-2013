(ns genmo.story
  (:require [genmo.data.plots :as plots]
            [genmo.plot :as plot]))

(defn create
  []
  (-> (plots/any)
    (plot/set-up)))
