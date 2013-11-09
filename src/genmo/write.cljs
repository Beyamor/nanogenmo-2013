(ns genmo.write)

(defn story
  [{:keys [plot]}]
  (->>
    [:exposition :rising-action :climax :falling-action :denoument]
    (map (fn [section]
           {:section section
            :content (get plot section)}))))
