CREATE TABLE roles (

    id BIGSERIAL PRIMARY KEY,

    role_name VARCHAR(30) NOT NULL UNIQUE

);

CREATE TABLE user_credential_roles (

    user_id BIGINT NOT NULL,

    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
        REFERENCES user_credentials(user_id),

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)

);

INSERT INTO roles(role_name)
VALUES
('USER'),
('ADMIN');