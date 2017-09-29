-- :name create-user! :! :n
-- :doc Creates a new user record. Requires :id, :first-name, :last-name, :is-active.
INSERT INTO users
(id, first_name, last_name, is_active)
VALUES (:id, :first-name, :last-name, :is-active)

-- :name create-bookmark! :! :n
-- :doc Creates a new bookmark record. Requires :url, :uuid, :owner, :descr, :last-changed.
INSERT INTO bookmarks
(url, uuid, owner, descr, last_changed)
VALUES (:url, :uuid, :owner, :descr, :last-changed)

-- :name create-class! :! :n
-- :doc Creates a new class record. Requires :uuid, :owner, :descr, :background, :foreground, :last-changed.
INSERT INTO classes
(uuid, owner, descr, background, foreground, last_changed)
VALUES (:uuid, :owner, :descr, :background, :foreground, :last-changed)

-- :name update-user! :! :n
-- :doc Updates an existing user record. Requires :id, :first-name, :last-name, :last-login, :is-active.
UPDATE users
SET first_name = :first-name, last_name = :last-name, last_login = :last-login, is_active = :is-active
WHERE id = :id

-- :name update-bookmark-all! :! :n
-- :doc Updates an existing bookmark record. Requires :id, :url, :uuid, :owner, :descr, :last-changed.
UPDATE bookmarks
SET url = :url, uuid = :uuid, owner = :owner, descr = :descr, last_changed = :last-changed
WHERE id = :id

-- :name update-class-all! :! :n
-- :doc Updates an existing class record. Requires :uuid, :owner, :descr, :background, :foregound, :last-changed.
UPDATE classes
SET uuid = :uuid, owner = :owner, descr = :descr, background = :background, foregroung = :foregroung, last_changed = :last-changed
WHERE id = :id

-- :name update-bookmark! :! :n
-- :doc Updates an existing bookmark record. Requires :uuid, :url, :descr, :last-changed.
UPDATE bookmarks
SET url = :url, descr = :descr, last_changed = :last-changed
WHERE uuid = :uuid

-- :name update-bookmark-url! :! :n
-- :doc Updates the `url` field on an existing bookmark record. Requires :id, :url.
UPDATE bookmarks
SET url = :url
WHERE ID = :id

-- :name update-bookmark-uuid! :! :n
-- :doc Updates the `uuid` field on an existing bookmark record. Requires :id, :uuid.
UPDATE bookmarks
SET uuid = :uuid
WHERE ID = :id

-- :name update-bookmark-descr! :! :n
-- :doc Updates the `descr` field of an existing bookmark record. Requires :id, :descr.
UPDATE bookmarks
SET descr = :descr
WHERE ID = :id

-- :name update-bookmark-owner! :! :n
-- :doc Updates the `owner` field of an existing bookmark record. Requires :id, :owner.
UPDATE bookmarks
SET owner = :owner
WHERE ID = :id

-- :name update-bookmark-last-changed! :! :n
-- :doc Updates the `last-changed` field of an existing bookmark record. Requires :id, :last-changed.
UPDATE bookmarks
SET last_changed = :last-changed
WHERE ID = :id

-- :name all-bookmarks :? :*
-- :doc Retrieves all the bookmarks.
SELECT * FROM bookmarks

-- :name user-bookmarks :? :*
-- :doc Retrieves bookmarks owner by given user. Requires :owner.
SELECT * FROM bookmarks
WHERE owner = :owner

-- :name get-user :? :1
-- :doc Retrieves a user given the id. Requires :id.
SELECT * FROM users
WHERE id = :id

-- :name get-bookmark :? :1
-- :doc Retrieves a bookmark given the id. Requires :id.
SELECT * FROM bookmarks
WHERE id = :id

-- :name get-user-by-uuid :? :1
-- :doc Retrieves a user given the uuid. Requires :uuid.
SELECT * FROM users
WHERE uuid = :uuid

-- :name get-bookmark-by-uuid :? :1
-- :doc Retrieves a bookmark given the uuid. Requires :uuid.
SELECT * FROM bookmarks
WHERE uuid = :uuid

-- :name get-class-by-uuid :? :1
-- :doc Retrieves a class the uuid. Requires :uuid.
SELECT * FROM classes
WHERE uuid = :uuid

-- :name delete-user! :! :n
-- :doc Deletes a user given the id. Requires :id.
DELETE FROM users
WHERE id = :id

-- :name delete-bookmark! :! :n
-- :doc Deletes a bookmark given the id. Requires :id.
DELETE FROM bookmarks
WHERE id = :id
