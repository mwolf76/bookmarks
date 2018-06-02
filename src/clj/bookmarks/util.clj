(ns bookmarks.util
  (:require [clojure.tools.logging :as log]))

(defn valid-color-string?
  [s]
  (let [valid-color-digit?
        (fn [d]
          (let [tmp (int d)]
            (or
             (and (<= 48 tmp) (<= tmp 57))
             (and (<= 65 tmp) (<= tmp 70)))))]
    (and
     (= 7 (count s))
     (= \# (first s)))
    (every? valid-color-digit?
            (clojure.string/upper-case (subs s 1)))))
