(ns genmo.data.plots
  (:use [genmo.text :only [sen sens par]])
  (:require [clojure.set :as s]))

(defn fight
  [{:keys [winner participants]}]
  (let [[guy1 guy2] participants
        loser       (-> participants set (disj winner) first)]
    (list
      (par
        (for [i (-> 2 (+ (rand-int 3)) range)
              :let [[from to] (if (< (rand) 0.5)
                                [guy1 guy2]
                                [guy2 guy1])]]
          (sen
            from #{"hit" "smacked" "whacked"} to)))
      (par
        (sen
          winner "deals" loser "a" #{"mighty" "powerful" "fierce"} #{"blow" "strike"} ", defeating" loser)))))

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
      :characters   #{:hero :parent-of-damsel}
      :description  [(par
                        (sen
                          :hero "enters" :home-of-damsel))
                      (par
                        (sen
                          :parent-of-damsel"explains to" :hero "that" :villain "kidnapped" :damsel))]}

     {:name         "Kidnapping Fight"
      :setting      {:location :home-of-villain}
      :characters   #{:hero :villain}
      :requires     [{:condition  :at-location
                      :who        :hero}]
      :description  [(par
                      (sen
                        :hero "reaches" :home-of-villain))
                     (par
                       (sen
                         :hero "fights" :villain))
                     (fight
                       {:participants [:hero :villain]
                        :winner       :hero})]}

     {:name         "Kidnapping Rescue"
      :setting      {:location :home-of-damsel}
      :characters   #{:hero :damsel :parent-of-damsel}
      :requires     [{:condition :at-location
                      :who       [:hero :damsel]}]
      :description  [(par
                       (sens
                         [:parent-of-damsel "thanks" :hero]
                         [:hero "and" :damsel "live happily ever after"]))]}]}])

(defn any
  []
  (rand-nth all))
