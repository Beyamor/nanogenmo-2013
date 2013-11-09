(ns genmo.data.plots)

(def all
  [; kidnapping
   {:requirements
    {:characters
     {:hero
      {:type :human}

      :villain
      {:type :monster}

      :damsel
      {:type :human}

      :parent-of-damsel
      {:type :human}}

     :locations
     {:start-location ["Town" "Castle"]
      :lair-of-villain ["Cave" "Mountain" "Tower" "Maze"]}}

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
