(ns genmo.data.plots)

(def all
  [; kidnapping
   {:requirements
    {:characters
     {:hero
      {:tags #{:human}}

      :villain
      {:tags #{:monster}}

      :damsel
      {:tags #{:human
               :royal}}}}

    :sections
    {:exposition
     [[:villain "kidnaps" :damsel]
      [:hero "enters" :starting-location]
      [{:parent-of :damsel} "explains to" :hero "that" :villain "kidnapped" :damsel]]

     :rising-action
     [:hero "tracks" :villain]

     :climax
     [[:hero "reaches" {:home-of :villain}]
      [:hero "fights" :villain]]

     :falling-action
     [:villain "is defeated"]

     :denoument
     [[:hero "carries" :damsel "out of" {:home-of :villain}]
      [{:parent-of :damsel} "thanks" :hero]
      [:hero "and" :damsel "live happily ever after"]]}}])

(defn any
  []
  (rand-nth all))
