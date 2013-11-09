(ns genmo.present)

(defn section
  [{:keys [section content]}]
  (str
    "<h1>" (name section) "</h1>"
    "<div>" content "</content>"))

(defn story
  [story]
  (->> story
    (map section)
    (apply str)))
