(ns bookmarks.test.db.core
  (:require [bookmarks.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [bookmarks.config :refer [env]]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'bookmarks.config/env
      #'bookmarks.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)

    (is (= 1 (db/create-user!
              t-conn {:id         "thomas.mann@gmail.com"
                      :first-name "Thomas"
                      :last-name  "Mann"
                      :is-active  false})))

    (is (= {:id         "thomas.mann@gmail.com"
            :first-name "Thomas"
            :last-name  "Mann"
            :last-login nil
            :is-active  false}

           (db/get-user
            t-conn {:id "thomas.mann@gmail.com"})))

    (is (= 1 (db/update-user!
              t-conn {:id         "thomas.mann@gmail.com"
                      :first-name "Thomas"
                      :last-name  "Mann"
                      :last-login nil
                      :is-active  true})))

    (is (= {:id         "thomas.mann@gmail.com"
            :first-name "Thomas"
            :last-name  "Mann"
            :last-login nil
            :is-active  true}

           (db/get-user t-conn
            {:id "thomas.mann@gmail.com"})))))

;; (deftest test-bookmarks
;;   (jdbc/with-db-transaction [t-conn *db*]
;;     (jdbc/db-set-rollback-only! t-conn)

;;     (is (= 1 (db/create-bookmark!
;;               t-conn {:url     "http://some.url"
;;                       :descr   "Some description"
;;                       :owner   "thomas.mann@gmail.com"
;;                       :last-changed (java.util.Date.)
;;                       :is-published false})))

;;     (is (= 1 (count (db/all-bookmarks t-conn))))))

;; (deftest test-bookmarks
;;   (jdbc/with-db-transaction [t-conn *db*]
;;     (jdbc/db-set-rollback-only! t-conn)
;;     (is (= 1 (db/create-user!
;;                t-conn
;;                {:id         "thomas.mann@gmail.com"
;;                 :first-name "Thomas"
;;                 :last-name  "Mann"
;;                 :is-active  true})))

;;     (is (= {:id         "thomas.mann@gmail.com"
;;             :first-name "Thomas"
;;             :last-name  "Mann"
;;             :last-login nil
;;             :is-active  true}
;;            (db/get-user t-conn {:id "thomas.mann@gmail.com"})))))

;;     (is (= 1 (db/create-user!
;;                t-conn
;;                {:id         "charles.darwin@gmail.com"
;;                 :first-name "Charlie"
;;                 :last-name  "Darwin"
;;                 :is-active  false})))

;;     (is (= {:id         "charles.darwin@gmail.com"
;;             :first-name "Charlie"
;;             :last-name  "Darwin"
;;             :last-login nil
;;             :is-active  false}
;;            (db/get-user t-conn {:id "charles.darwin@gmail.com"})))

;;     (is (= 1 (db/update-user!
;;               {:id         "charles.darwin@gmail.com"
;;                :first-name "Charles"
;;                :last-name "Darwin"
;;                :last-login nil
;;                :is-active true})))

;;     (is (= {:id         "charles.darwin@gmail.com"
;;             :first-name "Charles"
;;             :last-name  "Darwin"
;;             :last-login nil
;;             :is-active  true}
;;            (db/get-user t-conn {:id "charles.darwin@gmail.com"})))

;;     (is (= 1 (db/create-bookmark!
;;               t-conn
;;               {:url "https://en.wikipedia.org/wiki/Thomas_Mann"
;;                :descr "A vacuous description"
;;                :owner "thomas.mann@gmail.com"
;;                :last-changed (java.util.Date.)
;;                :is-published false
;;                })))

;;     (is (= 1 (db/create-bookmark!
;;               t-conn
;;               {:url "https://www.britannica.com/biography/Thomas-Mann"
;;                :descr "Another vacuous description"
;;                :owner "thomas.mann@gmail.com"
;;                :last-changed (java.util.Date.)
;;                :is-published false
;;                })))

;;     (is (= 1 (db/create-bookmark!
;;               t-conn
;;               {:url "https://en.wikipedia.org/wiki/Charles_Darwin"
;;                :descr "Wikipedia article"
;;                :owner "charles.darwin@gmail.com"
;;                :last-changed (java.util.Date.)
;;                :is-published false
;;                })))

;;     (is (every? (partial = "thomas.mann@gmail.com")
;;                 (map :owner (db/user-bookmarks{:owner "thomas.mann@gmail.com"}))))))
