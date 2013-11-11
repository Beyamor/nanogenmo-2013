(ns genmo.present)

(defn prepare-content
  [content]
  (-> content
    (clojure.string/replace "\n" "<br/>")))

(defn section
  [{:keys [section content]}]
  (str
    "<h1>" (name section) "</h1>"
    "<div>" (prepare-content content) "</content>"))

(defn story
  [story]
  (->> story
    (map section)
    (apply str)))
