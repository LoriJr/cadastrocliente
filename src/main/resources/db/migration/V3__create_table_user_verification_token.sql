CREATE TABLE user_verification_token (

    user_id BIGINT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiration TIMESTAMP NOT NULL,
    user_status BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_user_verification_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);