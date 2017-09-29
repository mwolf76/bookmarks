(ns bookmarks.controllers.core
  (:require [bookmarks.layout :as layout]
            [bookmarks.util :as util]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [bookmarks.db.core :as db]
            [struct.core :as st]))

;; the point of lisp is that the language allows you to build a dsl,
;; suiting better than ony language can, the problem at hand.
(defmacro log-uuid [& body]
  `(log/debug "uuid: " ~@body))

(defmacro log-data [& body]
  `(log/debug "data: " ~@body))

(defmacro notfound []
  `response/not-found)

;; validation schemas
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

(def class-schema
  [[:descr
    st/required
    st/string]

   [:background
    st/required
    st/string
    {:descr "background must be a valid HTML color"
     :validate util/valid-color-string?}]

   [:foreground
    st/required
    st/string
    {:descr "foreground must be a valid HTML color"
     :validate util/valid-color-string?}]])

(defn validate-class[params]
  (first (st/validate params class-schema)))

(defn home-page [req]
  (layout/render
   "home.html"
   (merge {:bookmarks (db/user-bookmarks
                       {:owner "not.me@yahoo.jp"})})))

(defn bookmarks-page [{:keys [flash]}]
  (layout/render
   "bookmarks.html"
   (merge {:bookmarks (db/user-bookmarks
                       {:owner "not.me@yahoo.jp"})}
          (select-keys flash [:name :message :errors]))))

(defn classes-page [{:keys [flash]}]
  (layout/render
   "classes.html"
   (merge {:classes (db/user-classes
                     {:owner "not.me@yahoo.jp"})}
          (select-keys flash [:name :message :errors]))))

(defn members-page [{:keys [flash]}]
  (layout/render
   "class-members.html"
   (merge {:members (db/get-bookmarks-by-class
                     {:owner "not.me@yahoo.jp"})}
          (select-keys flash [:name :message :errors]))))

(defn alter-members! [{:keys [params]} uuid]
  (log/debug (format "alter-members! %s" params)))

(defn about-page [req]
  (layout/render "about.html"))

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
      (notfound))))

(defn edit-class [{flash :flash} uuid]
  (log-uuid uuid)
  (log-data flash)
  (if (= "new" uuid)
    (layout/render "class.html" {})
    (if-let [class (db/get-class-by-uuid {:uuid uuid})]
      (layout/render "class.html"
                     (merge
                      (select-keys class [:uuid :url :descr :last-changed])
                      (select-keys flash [:name :message :errors])))
      (notfound))))

(defn save-bookmark! [{:keys [params]} uuid]
  (log/debug (format "save-bookmark! %s" params))
  (if-let [errors (validate-bookmark params)]
    (do
      (log/debug (format "validation errors: %s" errors))
      (-> (response/found "/")
          (assoc :flash
            (assoc params :errors errors))))
    (do
      (if (= uuid "new")
        (do
          (db/create-bookmark!
           (assoc params
             :uuid (java.util.UUID/randomUUID)
             :owner "not.me@yahoo.jp"
             :last-changed (org.joda.time.DateTime.)))
          (response/found "/bookmarks"))
        (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
          (do
            (db/update-bookmark! (assoc params
                                   :id (:id bookmark)
                                   :owner "not.me@yahoo.jp"
                                   :last-changed (org.joda.time.DateTime.)))
            (response/found "/bookmarks"))
          (notfound))))))

(defn save-class! [{:keys [params]} uuid]
  (log-uuid uuid)
  (log-data params)
  (if-let [errors (validate-class params)]
    (do (log/debug (format "validation errors: %s" errors))
        (-> (response/found "/classes")
            (assoc :flash
              (assoc params :errors errors))))
    (do (if (= uuid "new")
          (do (prn (db/create-class!
                    (assoc params
                      :uuid (java.util.UUID/randomUUID)
                      :owner "not.me@yahoo.jp"
                      :last-changed (org.joda.time.DateTime.))))
              (prn "Thomas"))
          (if-let [class (db/get-class-by-uuid {:uuid uuid})]
            (do (db/update-class! (assoc params
                                    :owner "not.me@yahoo.jp"
                                    :last-changed (org.joda.time.DateTime.)))
                (print "Thomas")
                (response/found "/classes"))
            (notfound))))))

(defn confirm-bookmark-deletion [req uuid]
  (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
    (layout/render "confirm-bookmark-deletion.html"
                   (select-keys bookmark
                                [:uuid :url :descr :last-changed]))
    (notfound)))

(defn delete-bookmark! [req uuid]
  (when-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
    (db/delete-bookmark!
     (select-keys bookmark [:id]))
    (response/found "/")))

(defn confirm-class-deletion [req uuid]
  (if-let [class (db/get-class-by-uuid {:uuid uuid})]
    (layout/render "confirm-class-deletion.html"
                   (select-keys class
                                [:uuid :label :descr :background :foreground :last-changed]))
    (notfound)))

(defn delete-class! [req uuid]
  (when-let [class (db/get-class-by-uuid {:uuid uuid})]
    (db/delete-class!
     (select-keys class [:id]))
    (response/found "/classes")))
