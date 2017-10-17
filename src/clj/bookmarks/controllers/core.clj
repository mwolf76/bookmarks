(ns bookmarks.controllers.core
  (:require [bookmarks.layout :as layout]
            [bookmarks.util :as util]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [bookmarks.db.core :as db]
            [struct.core :as st]))

(defn class-uuid?
  [uuid]
  (log/debug (format "Validating class uuid %s" uuid))
  (db/get-class-by-uuid {:uuid uuid}))

(defn vector-of-class-uuids? 
  [in]
  (let [v (into [] (flatten (vector in)))]
    (every? class-uuid? v)))

(def vector-of-class-uuids
  {:message "must be a vector instance"
   :optional true
   :validate vector-of-class-uuids?})

;; validation schemas
(def bookmark-schema
  [[:class-uuids
    st/required
    vector-of-class-uuids] 

   [:url
    st/required
    st/string]

   [:descr
    st/required
    st/string
    {:descr "descr must contain at least 10 characters"
     :validate #(> (count %) 9)}]])

(defn validate-bookmark[params]
  (log/debug (format "Validating bookmark %s" params))
  (first (st/validate params bookmark-schema)))

(def class-schema
  [[:label
    st/required
    st/string]
    
   [:foreground
    st/required
    st/string
    {:descr "foreground must be a valid HTML hexadecimal color string (e.g. #FEFEFE)" 
     :validate util/valid-color-string?}]

   [:background
    st/required
    st/string
    {:descr "background must be a valid HTML hexadecimal color string (e.g. #FEFEFE)" 
     :validate util/valid-color-string?}]
   ])

(defn validate-class[params]
  (first (st/validate params class-schema)))

(defn home-page [{:keys [params]}]
  (let [owner "not.me@yahoo.jp"
        classes (db/user-classes {:owner owner})
        bookmarks (db/user-bookmarks {:owner owner})
        
        bookmark->classes
        (fn[bookmark]
          (db/get-bookmark-classes {:bookmark-id (:id bookmark)}))

        labels
        (:labels params)

        is-class-active?
        (fn [class]
          (and 
           labels
           (seq
            (clojure.set/intersection 
             (into #{} (map #(java.util.UUID/fromString %) 
                            (clojure.string/split labels #" ")))
             (into #{} [(:uuid class)])))))
        
        is-bookmark-shown?
        (fn [bookmark]
          (or 
           (not labels)
           (seq
            (clojure.set/intersection 
             (into #{} (map #(java.util.UUID/fromString %) 
                            (clojure.string/split labels #" ")))
             (into #{} (map :uuid (:classes bookmark)))))))

        augmented-classes
        (map #(assoc % :selected (is-class-active? %)) classes)

        augmented-bookmarks
        (map #(assoc % :classes (bookmark->classes %)) bookmarks)
        ]

    (log/info labels)

    (layout/render
     "home.html"
     {:classes augmented-classes
      :bookmarks (filter is-bookmark-shown? augmented-bookmarks)})))

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
  (let [user-classes (db/user-classes {:owner "not.me@yahoo.jp"})]

    (if (= "new" uuid)
      ;; creating a new bookmark record
      (layout/render "bookmark.html" 
                     (merge {:uuid "new" :classes user-classes :back "bookmarks"}
                            (select-keys flash [:errors])))
      
      ;; updating an existing bookmark record
      (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
        (let [bookmark-id (:id bookmark)
              selected #(if (some #{(:id %)} (map :id (db/get-bookmark-classes {:bookmark-id bookmark-id}))) (assoc % :selected true) %)]
          (layout/render "bookmark.html"
                         (merge {:classes (map selected user-classes) :back "bookmarks"}
                                (select-keys bookmark [:uuid :url :descr :last-changed])
                                (select-keys flash [:errors]))))
        (response/not-found)))))

(defn edit-class [{flash :flash} uuid]
  (if (= "new" uuid)
    ;; creating a new class record
    (layout/render "class.html" 
                   (merge {:uuid "new" :back "classes"}
                          (select-keys flash [:label :foreground :background :errors])))
    
    ;; updating an existing class record
    (if-let [class (db/get-class-by-uuid {:uuid uuid})]
      (layout/render "class.html"
                     (merge {:back "classes"}
                      (select-keys class [:uuid :url :label :foreground :background :last-changed])
                      (select-keys flash [:name :message :errors])))
      
      (response/not-found))))

(defn save-bookmark! [{:keys [params]} uuid]
  (if-let [errors (validate-bookmark params)]
    (do (log/debug (format "validation errors: %s" errors))
        (-> (response/found "edit")
            (assoc :flash 
              (assoc params :errors errors))))

    ;; no validation errors
    (do (if (= uuid "new")
          ;; create a new record
          (let [bookmark-id (first (vals (db/create-bookmark!
                                          (assoc params
                                            :uuid (java.util.UUID/randomUUID)
                                            :owner "not.me@yahoo.jp"
                                            :last-changed (org.joda.time.DateTime.)))))]
            (log/debug (format "Created a new bookmark record (id = %d)" bookmark-id))

            ;; build initial bookmark-class associations
            (doseq [class (map #(db/get-class-by-uuid {:uuid %}) 
                               (into [] (flatten (vector (:class-uuids params)))))]
              (let [class-id (:id class)]
                (log/debug (format "Created bookmark-class association %d --> %d"
                                   bookmark-id class-id))
                (db/create-bookmark-class! {:bookmark-id bookmark-id :class-id class-id}))))

          ;; update an existing bookmark record
          (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
            
            (let [bookmark-id (:id bookmark)]
              (db/update-bookmark! (assoc params
                                     :id (:id bookmark)
                                     :owner "not.me@yahoo.jp"
                                     :last-changed (org.joda.time.DateTime.)))
              
              ;; rewrite bookmark-class associations
              (log/debug (format "Clearing bookmark-class associations for bookmark (id = %d)" 
                                 bookmark-id))
              (db/clear-bookmark-classes! {:bookmark-id bookmark-id})
              (doseq [class (map #(db/get-class-by-uuid {:uuid %}) 
                                 (into [] (flatten (vector (:class-uuids params)))))]
                (let [class-id (:id class)]
                  (log/debug (format "Created bookmark-class association %d --> %d"
                                     bookmark-id class-id))
                  (db/create-bookmark-class! 
                   {:bookmark-id bookmark-id :class-id class-id}))))
            
            (response/not-found)))
    
        (response/found "/bookmarks"))))

(defn save-class! [{:keys [params]} uuid]
  (log/debug params)
  (if-let [errors (validate-class params)]
    (do (log/debug (format "validation errors: %s" errors))
        (-> (response/found "edit")
            (assoc :flash 
              (assoc params :errors errors))))
  
    ;; no validation errors
    (do (if (= uuid "new")
          ;; create a new record
          (let [class-id (first (vals (db/create-class!
                                       (assoc params
                                         :uuid (java.util.UUID/randomUUID)
                                         :owner "not.me@yahoo.jp"
                                         :last-changed (org.joda.time.DateTime.)))))]
            (log/debug (format "Created a new class record (id = %d)" class-id)))
          
          ;; update an existing record
          (if-let [class (db/get-class-by-uuid {:uuid uuid})]
            (let [class-id (:id class)]
              (db/update-class! (assoc params
                                  :id class-id
                                  :owner "not.me@yahoo.jp"
                                  :last-changed (org.joda.time.DateTime.)))
              (log/debug (format "Upated class record (id = %d)" class-id)))
            
            (response/not-found)))

        (response/found "/classes"))))

(defn confirm-bookmark-deletion [req uuid]
  (if-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
    (layout/render "confirm-bookmark-deletion.html"
                   (merge {:back "bookmarks"} 
                          (select-keys bookmark
                                       [:uuid :url :descr :last-changed])))
    (response/not-found)))

(defn delete-bookmark! [req uuid]
  (when-let [bookmark (db/get-bookmark-by-uuid {:uuid uuid})]
    (db/delete-bookmark!
     (select-keys bookmark [:id]))
    (response/found "/")))

(defn confirm-class-deletion [req uuid]
  (if-let [class (db/get-class-by-uuid {:uuid uuid})]
    (layout/render "confirm-class-deletion.html"
                   (merge {:back "classes"}
                          (select-keys class
                                       [:uuid :label :descr :background :foreground :last-changed])))
    (response/not-found)))

(defn delete-class! [req uuid]
  (when-let [class (db/get-class-by-uuid {:uuid uuid})]
    (db/delete-class!
     (select-keys class [:id]))
    (response/found "/classes")))

