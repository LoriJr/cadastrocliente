ALTER TABLE user_credential_roles
DROP CONSTRAINT fk_user_roles_user;

ALTER TABLE user_credential_roles
ADD CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id)
    REFERENCES user_credentials(user_id)
    ON DELETE CASCADE;