CREATE TABLE user_credentials (
    user_id INTEGER PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_credentials_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);