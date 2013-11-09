(ns genmo.write)

(defn realize-section
  [definition details]
  (->>
    (for [piece definition]
      (cond
        (string? piece)
        piece

        (keyword? piece)
        (get-in details [piece :name])))
    (apply str)))

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
              (realize-section details))}))))
