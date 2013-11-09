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
      {:tags #{:human}}

      :parent-of-damsel
      {:tags #{:human}}}

     :locations
     {:start-location
      {:tags #{:human}}

      :lair-of-villain
      {:tags #{:monster}}}}

    :plot
    {:exposition
     [[:villain " kidnaps " :damsel]
      [:hero " enters " :starting-location]
      [:parent-of-damsel " explains to " :hero " that " :villain " kidnapped " :damsel]]

     :rising-action
     [:hero " tracks " :villain]

     :climax
     [[:hero " reaches " :lair-of-villain]
      [:hero " fights " :villain]]

     :falling-action
     [:villain " is defeated"]

     :denoument
     [:hero " and " :damsel " live happily ever after"]}}])

(defn any
  []
  (rand-nth all))
