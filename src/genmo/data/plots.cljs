(ns genmo.data.plots)

(def all
  [; kidnapping
   {:requirements
    {:characters
     {:hero
      #{:human}

      :villain
      #{:monster}

      :damsel
      #{:human :royal}}}

    :sections
    {:exposition
     [[:villain "kidnaps" :damsel]
      [:hero "enters" {:home-of :damsel}]
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
      [:hero "returns to" {:home-of :damsel}]
      [{:parent-of :damsel} "thanks" :hero]
      [:hero "and" :damsel "live happily ever after"]]}}])

(defn any
  []
  (rand-nth all))
