(ns genmo.story
  (:require [genmo.data.plots :as plots]
            [genmo.plot :as plot]))

(defn create
  []
  (let [the-plot (plots/any)]
    (->
      {:events (:events the-plot)}
      (assoc :details (plot/set-up the-plot)))))
