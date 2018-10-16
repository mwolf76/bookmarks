(ns bookmarks.util)

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

(defn make-short-url
  [url]
  (str (subs url 0 (min 40 (count url))) " ..."))
