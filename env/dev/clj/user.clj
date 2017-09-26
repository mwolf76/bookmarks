(ns user
  (:require [mount.core :as mount]
            bookmarks.core))

(defn start []
  (mount/start-without #'bookmarks.core/repl-server))

(defn stop []
  (mount/stop-except #'bookmarks.core/repl-server))

(defn restart []
  (stop)
  (start))


