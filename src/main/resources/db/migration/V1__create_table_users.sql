CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    rg VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,

    -- Endereço
    address_line1 VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    address_line2 VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_cpf UNIQUE (cpf),
    CONSTRAINT uk_users_rg UNIQUE (rg)
);