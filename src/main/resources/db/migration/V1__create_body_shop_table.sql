CREATE TABLE body_shop (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    name VARCHAR(100),
    sido VARCHAR(255),
    sigungu VARCHAR(255),
    bname VARCHAR(255),
    jibun_address VARCHAR(255),
    road_address VARCHAR(255),
    detail VARCHAR(255),
    description VARCHAR(200),
    phone_number VARCHAR(15) NOT NULL,
    link VARCHAR(200),
    accept_count INT NOT NULL,
    pickup_available TINYINT(1) NOT NULL,
    manager_user_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);
