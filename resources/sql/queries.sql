-- :name create-bookmark! :i!
-- :doc Creates a new bookmark record. Requires :uuid, :url, :descr, :last-changed.
INSERT INTO bookmarks
(uuid, url, descr, last_changed)
VALUES (:uuid, :url, :descr, :last-changed)

-- :name create-class! :i!
-- :doc Creates a new class record. Requires :uuid, :label, :foreground, :background, :last-changed.
INSERT INTO classes
(uuid, label, foreground, background, last_changed)
VALUES (:uuid, :label, :foreground, :background, :last-changed)

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
SELECT id, uuid, url, descr, last_changed 
FROM bookmarks_classes JOIN bookmarks
WHERE id = bookmark_id and class_id = :class-id;

-- :name get-bookmark-classes :? :*
-- :doc Retrieves all the classes the given bookmark belongs to. Requires :bookmark-id.
SELECT id, uuid, label, foreground, background, last_changed
FROM bookmarks_classes JOIN classes
WHERE id = class_id AND bookmark_id = :bookmark-id;

-- :name update-bookmark! :! :n
-- :doc Updates an existing bookmark record. Requires :id, :url, :descr, :priority and :last-changed
UPDATE bookmarks
SET url = :url, descr = :descr, priority = :priority, last_changed = :last-changed
WHERE id = :id

-- :name update-class! :! :n
-- :doc Updates an existing class record. Requires :id, :label, :foreground, :background, :last-changed.
UPDATE classes
SET label = :label, foreground = :foreground, background = :background, last_changed = :last-changed
WHERE id = :id

-- :name update-class-all! :! :n
-- :doc Updates an existing class record rewriting all fields. Requires :id, :uuid, :label, :descr, :last-changed.
UPDATE classes
SET uuid = :uuid, label = :label, descr = :descr, last_changed = :last-changed
WHERE id = :id

-- :name all-bookmarks :? :*
-- :doc Retrieves all the bookmarks.
SELECT * FROM bookmarks

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

-- :name delete-bookmark! :! :n
-- :doc Deletes a bookmark given the id. Requires :id.
DELETE FROM bookmarks
WHERE id = :id

-- :name delete-class! :! :n
-- :doc Deletes a class given the id. Requires :id.
DELETE FROM classes
WHERE id = :id

