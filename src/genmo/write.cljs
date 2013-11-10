(ns genmo.write)

(defn realize
  [thing details]
  (cond
    (string? thing)
    thing

    (or (map? thing) (keyword? thing))
    (let [detail (get details thing)]
      (if (map? detail)
        (:name detail)
        detail))

    (vector? thing)
    (-> thing
      (->>
        (map #(realize % details))
        (interpose " ")
        (apply str))
      (str "\n"))))

(defn story
  [{:keys [plot details]}]
  (->>
    [:exposition :rising-action :climax :falling-action :denoument]
    (map (fn [section]
           {:section
            section

            :content
            (-> plot
              (get section)
              (realize details))}))))
