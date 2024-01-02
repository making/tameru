CREATE TABLE IF NOT EXISTS log_event
(
    event_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    message   TEXT,
    timestamp DATETIME                          NOT NULL,
    metadata  JSON
);

CREATE INDEX log_event_timestamp ON log_event(timestamp);

CREATE VIRTUAL TABLE log_event_fts USING fts5
(
    message,
    content='log_event',
    content_rowid='event_id',
    tokenize='trigram'
);

CREATE TRIGGER log_event_ai
    AFTER INSERT
    ON log_event
BEGIN
    INSERT INTO log_event_fts (rowid, message) VALUES (new.event_id, new.message);
END;

CREATE TRIGGER log_event_au
    AFTER UPDATE
    ON log_event
BEGIN
    INSERT INTO log_event_fts (log_event_fts, rowid, message) VALUES ('delete', old.event_id, old.message);
    INSERT INTO log_event_fts (rowid, message) VALUES (new.event_id, new.message);
END;

CREATE TRIGGER log_event_ad
    AFTER DELETE
    ON log_event
BEGIN
    INSERT INTO log_event_fts (log_event_fts, rowid, message) VALUES ('delete', old.event_id, old.message);
END;
