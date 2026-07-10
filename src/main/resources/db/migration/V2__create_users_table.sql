CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    login_id VARCHAR(30),
    password VARCHAR(200) NOT NULL,
    email VARCHAR(30),
    name VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    body_shop_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_login_id UNIQUE (login_id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_body_shop
        FOREIGN KEY (body_shop_id) REFERENCES body_shop (id)
);
