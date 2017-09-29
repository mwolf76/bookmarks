CREATE TABLE classes
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 uuid UUID,
 owner VARCHAR(30),
 descr VARCHAR(2048),
 background CHAR(6),
 foreground CHAR(6),
 last_changed TIMESTAMP);

CREATE TABLE bookmarks_classes
(bookmark_id INTEGER,
 FOREIGN KEY(bookmark_id) REFERENCES bookmarks(id),
 class_id INTEGER,
 FOREIGN KEY(class_id) REFERENCES classes(id),
 PRIMARY KEY (bookmark_id, class_id));
