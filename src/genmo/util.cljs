(ns genmo.util)

(defn insertv [v pos item]
  (apply conj (subvec v 0 pos) item (subvec v pos)))

(def flatten-1 (partial apply concat))
