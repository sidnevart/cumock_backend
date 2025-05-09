ALTER TABLE users
    ADD COLUMN username VARCHAR(50) NOT NULL DEFAULT '';

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE(username);
