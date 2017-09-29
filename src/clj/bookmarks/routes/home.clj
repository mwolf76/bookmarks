(ns bookmarks.routes.home
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [redirect]]
            [bookmarks.controllers.core :as controllers]))

(defroutes home-routes
  (GET "/" req (redirect "/bookmarks"))
  (GET "/bookmarks" req controllers/bookmarks-page)
  (GET "/classes" req controllers/classes-page)
  (GET "/about" req controllers/about-page)

  ;; bookmarks management (add/edit/delete)
  (GET  "/bookmark/:uuid" [uuid :as req] (controllers/edit-bookmark req uuid))
  (POST "/bookmark/:uuid" [uuid :as req] (controllers/save-bookmark! req uuid))
  (GET  "/bookmark/:uuid/delete" [uuid :as req] (controllers/confirm-bookmark-deletion req uuid))
  (POST "/bookmark/:uuid/delete" [uuid :as req] (controllers/delete-bookmark! req uuid))

  ;; classes management (add/edit/delete)
  (GET  "/class/:uuid" [uuid :as req] (controllers/edit-class req uuid))
  (POST "/class/:uuid" [uuid :as req] (controllers/save-class! req uuid))
  (GET  "/class/:uuid/delete" [uuid :as req] (controllers/confirm-class-deletion req uuid))
  (POST "/class/:uuid/delete" [uuid :as req] (controllers/delete-class! req uuid))

  ;; bookmark-class relationship management
  (GET  "/class/:uuid/members" [uuid :as req] (controllers/members-page req uuid))
  (POST "/class/:uuid/members" [uuid :as req] (controllers/alter-members! req uuid)))


