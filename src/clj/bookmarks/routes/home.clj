(ns bookmarks.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [bookmarks.controllers.core :as controllers]))

(defroutes home-routes
  (GET "/" req controllers/home-page)
  (GET "/about" req controllers/about-page)

  ;; bookmarks management (add/edit/delete)
  (GET  "/bookmark/:uuid" [uuid :as req] (controllers/edit-bookmark req uuid))
  (POST "/bookmark/:uuid" [uuid :as req] (controllers/save-bookmark! req uuid))

  (GET  "/bookmark/:uuid/delete" [uuid :as req] (controllers/delete-bookmark req uuid))
  (POST "/bookmark/:uuid/delete" [uuid :as req] (controllers/delete-bookmark! req uuid)))
