(ns bookmarks.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[bookmarks started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[bookmarks has shut down successfully]=-"))
   :middleware identity})
