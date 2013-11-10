(ns genmo.lang)

(defn refer-to
  [thing]
  (cond
    (string? thing)
    thing

    (map? thing)
    (cond
      (:name thing)
      (:name thing)

      (:term thing)
      (str "the " (:term thing)))))
