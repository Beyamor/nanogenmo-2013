(ns genmo.data.plots)

(def all
  [; kidnapping
   {:requires
    {:characters
     [[:hero
       {:type :human}]

      [:villain
       {:type :monster}]

      [:damsel
       {:type :human}]]}

    :plot
    {:exposition
     [:villain " kidnaps " :damsel]

     :rising-action
     [:hero " tracks down " :villain]

     :climax
     [:hero " fights " :villain]

     :falling-action
     [:villain " is defeated"]

     :denoument
     [:hero " and " :damsel " live happily ever after"]}}])

(defn any
  []
  (rand-nth all))
