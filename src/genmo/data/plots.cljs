(ns genmo.data.plots)

(defn fight
  [{:keys [winner participants]}]
  (let [[guy1 guy2] participants
        loser       (-> participants set (disj winner) first)]
    (for [i (-> 2 (+ (rand-int 3)) range)
          :let [[from to] (if (< (rand) 0.5)
                            [guy1 guy2]
                            [guy2 guy1])]]
      [from #{"hit" "smacked" "whacked"} to])
    [winner "deals" loser "a" #{"mighty" "powerful" "fierce"} #{"blow" "strike"} ", defeating" loser]))

(def all
  [; kidnapping
   {:characters
    {:hero
     #{:human}

     :villain
     #{:monster}

     :damsel
     #{:human :royal}}

    :events
    [{:name         "Kidnapping Introduction"
      :setting      {:location :home-of-damsel}
      :characters   [:hero :parent-of-damsel]
      :description  [[:hero "enters" :home-of-damsel]
                     [:parent-of-damsel"explains to" :hero "that" :villain "kidnapped" :damsel]]}

     {:name         "Kidnapping Fight"
      :setting      {:location :home-of-villain}
      :characters   [:hero :villain]
      :description  [[:hero "reaches" :home-of-villain]
                     [:hero "fights" :villain]
                     (fight
                       {:participants [:hero :villain]
                        :winner       :hero})]}

     {:name         "Kidnapping Rescue"
      :setting      {:location :home-of-damsel}
      :characters   [:hero :damsel :parent-of-damsel]
      :description  [[:parent-of-damsel "thanks" :hero]
                     [:hero "and" :damsel "live happily ever after"]]}]}])

(defn any
  []
  (rand-nth all))
