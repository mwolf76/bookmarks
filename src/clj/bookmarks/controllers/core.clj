(ns bookmarks.controllers.core
  (:require [bookmarks.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [bookmarks.db.core :as db]
            [ring.util.response :refer [redirect]]
            [struct.core :as st]))

(defn home-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge 
    {:bookmarks (db/user-bookmarks 
                 {:owner "marco.pensallorto@gmail.com"})}
          (select-keys 
           flash [:name :message :errors]))))

(defn about-page [req]
  (layout/render "about.html"))

(def bookmark-schema
  [[:url
    st/required
    st/string]

   [:descr
    st/required
    st/string
    {:descr "descr must contain at least 10 characters"
     :validate #(> (count %) 9)}]])

(defn validate-bookmark[params]
  (first (st/validate params bookmark-schema)))

(defn edit-bookmark [{flash :flash} uuid]
  (log/debug uuid)
  (log/debug flash)
  (if (= "new" uuid)
    (layout/render "bookmark.html" {})
    (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
      (layout/render "bookmark.html"
                   (merge 
                    (select-keys bookmark [:uuid :url :descr :last-changed])
                    (select-keys flash [:name :message :errors])))
      (response/not-found))))

(defn save-bookmark! [{:keys [params]} uuid]
  (log/debug params)
  (log/debug uuid)
  (if-let [errors (validate-bookmark params)]
    (-> (response/found "/")
        (assoc :flash 
          (assoc params :errors errors)))
    (do
      (if (= uuid "new")
        (db/create-bookmark!
         (assoc params 
           :uuid (java.util.UUID/randomUUID)
           :owner "marco.pensallorto@gmail.com"
           :last-changed (org.joda.time.DateTime.)))
        (db/update-bookmark!
         (assoc params 
           :last-changed (org.joda.time.DateTime.))))
      (response/found "/"))))

(defn delete-bookmark [req uuid]
  (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
      (layout/render "confirm-deletion.html" 
                     (select-keys bookmark 
                                  [:uuid :url :descr :last-changed]))
      (response/not-found)))

(defn delete-bookmark! [req uuid]
  (when-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
    (db/delete-bookmark! 
     (select-keys bookmark [:id]))
    (response/found "/")))
