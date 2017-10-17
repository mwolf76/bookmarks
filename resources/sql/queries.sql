-- :name create-user! :i!
-- :doc Creates a new user record. Requires :id, :first-name, :last-name, :is-active.
INSERT INTO users
(id, first_name, last_name, is_active)
VALUES (:id, :first-name, :last-name, :is-active)

-- :name create-bookmark! :i!
-- :doc Creates a new bookmark record. Requires :uuid, :url, :owner, :descr, :last-changed.
INSERT INTO bookmarks
(uuid, url, owner, descr, last_changed)
VALUES (:uuid, :url, :owner, :descr, :last-changed)

-- :name create-class! :i!
-- :doc Creates a new class record. Requires :uuid, :owner, :label, :foreground, :background, :last-changed.
INSERT INTO classes
(uuid, owner, label, foreground, background, last_changed)
VALUES (:uuid, :owner, :label, :foreground, :background, :last-changed)

-- :name create-bookmark-class! :! :n
-- :doc Creates a new bookmark-class association. Requires :bookmark_id, :class_id.
INSERT INTO bookmarks_classes
(bookmark_id, class_id)
VALUES (:bookmark-id, :class-id)

-- :name clear-bookmark-classes! :! :n
-- :doc Deletes all class associations to given bookmark. Requires :bookmark_id.
DELETE FROM bookmarks_classes
WHERE bookmark_id = :bookmark-id

-- :name get-bookmarks-by-class :? :*
-- :doc Retrieves all bookmarks associated with given class. Requires :class-id.
SELECT id, uuid, url, descr, owner, last_changed 
FROM bookmarks_classes JOIN bookmarks
WHERE id = bookmark_id and class_id = :class-id;

-- :name get-bookmark-classes :? :*
-- :doc Retrieves all the classes the given bookmark belongs to. Requires :bookmark-id.
SELECT id, uuid, label, owner, foreground, background, last_changed
FROM bookmarks_classes JOIN classes
WHERE id = class_id AND bookmark_id = :bookmark-id;

-- :name update-user! :! :n
-- :doc Updates an existing user record. Requires :id, :first-name, :last-name, :last-login, :is-active.
UPDATE users
SET first_name = :first-name, last_name = :last-name, last_login = :last-login, is_active = :is-active
WHERE id = :id

-- :name update-user-all! :! :n
-- :doc Updates an existing user record rewriting all fields. Requires :id, :uuid, :first-name, :last-name. :last-login, :is-active.
UPDATE users
SET uuid = :uuid, first_name = :first-name, last_name = :last-name, last_login = :last-login, is-active = :is_active
WHERE id = :id

-- :name update-bookmark! :! :n
-- :doc Updates an existing bookmark record. Requires :id, :url, :owner, :descr, :last-changed
UPDATE bookmarks
SET url = :url, owner = :owner, descr = :descr, last_changed = :last-changed
WHERE id = :id

-- :name update-bookmark-all! :! :n
-- :doc Updates an existing bookmark record rewriting all fields. Requires :id, :uuid, :url, :owner, :descr, :last-changed.
UPDATE bookmarks
SET uuid = :uuid, url = :url, owner = :owner, descr = :descr, last_changed = :last-changed
WHERE id = :id

-- :name update-class! :! :n
-- :doc Updates an existing class record. Requires :id, :owner, :label, :foreground, :background, :last-changed.
UPDATE classes
SET owner = :owner, label = :label, foreground = :foreground, background = :background, last_changed = :last-changed
WHERE id = :id

-- :name update-class-all! :! :n
-- :doc Updates an existing class record rewriting all fields. Requires :id, :uuid, :owner, :label, :descr, :last-changed.
UPDATE classes
SET uuid = :uuid, owner = :owner, label = :label, descr = :descr, last_changed = :last-changed
WHERE id = :id

-- :name all-users :? :*
-- :doc Retrieves all the users.
SELECT * FROM users

-- :name get-user :? :1
-- :doc Retrieves a user given the id. Requires :id.
SELECT * FROM users
WHERE id = :id

-- :name get-user-by-uuid :? :1
-- :doc Retrieves a user given the uuid. Requires :uuid.
SELECT * FROM users
WHERE uuid = :uuid

-- :name all-bookmarks :? :*
-- :doc Retrieves all the bookmarks.
SELECT * FROM bookmarks

-- :name user-bookmarks :? :*
-- :doc Retrieves bookmarks owner by given user. Requires :owner.
SELECT * FROM bookmarks
WHERE owner = :owner

-- :name get-bookmark :? :1
-- :doc Retrieves a bookmark given the id. Requires :id.
SELECT * FROM bookmarks
WHERE id = :id

-- :name get-bookmark-by-uuid :? :1
-- :doc Retrieves a bookmark given the uuid. Requires :uuid.
SELECT * FROM bookmarks
WHERE uuid = :uuid

-- :name all-classes :? :*
-- :doc Retrieves all the classes
SELECT * FROM classes

-- :name user-classes :? :*
-- :doc Retrieves classes owner by given user. Requires :owner.
SELECT * FROM classes
WHERE owner = :owner

-- :name get-class :? :1
-- :doc Retrieves class given the id. Requires :id.
SELECT * FROM classes
WHERE id = :id

-- :name get-class-by-uuid :? :1
-- :doc Retrieves a class the uuid. Requires :uuid.
SELECT * FROM classes
WHERE uuid = :uuid

-- :name class-members :? :*
-- :doc Retrieves class members for the given id. Requires :id.
SELECT * FROM bookmarks JOIN classes
WHERE 

-- :name delete-user! :! :n
-- :doc Deletes a user given the id. Requires :id.
DELETE FROM users
WHERE id = :id

-- :name delete-bookmark! :! :n
-- :doc Deletes a bookmark given the id. Requires :id.
DELETE FROM bookmarks
WHERE id = :id

-- :name delete-class! :! :n
-- :doc Deletes a class given the id. Requires :id.
DELETE FROM classes
WHERE id = :id

