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
      [:hero "enters" :home-of-damsel]
      [:parent-of-damsel"explains to" :hero "that" :villain "kidnapped" :damsel]]

     :rising-action
     [:hero "tracks" :villain]

     :climax
     [[:hero "reaches" :home-of-villain]
      [:hero "fights" :villain "!"]
      (for [i (range (+ 2 (rand-int 3)))
            :let [[from to] (if (< (rand) 0.5)
                              [:hero :villain]
                              [:villain :hero])]]
        [from "hits" to])
      [:hero "deals" :villain "a" #{"mighty" "great" "powerful"} #{"blow" "hit" "strike"}]]

     :falling-action
     [:villain "is defeated"]

     :denoument
     [[:hero "carries" :damsel "out of" :home-of-villain]
      [:hero "returns to" :home-of-damsel]
      [:parent-of-damsel "thanks" :hero]
      [:hero "and" :damsel "live happily ever after"]]}}])

(defn any
  []
  (rand-nth all))
