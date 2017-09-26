(ns bookmarks.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [bookmarks.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[bookmarks started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[bookmarks has shut down successfully]=-"))
   :middleware wrap-dev})
