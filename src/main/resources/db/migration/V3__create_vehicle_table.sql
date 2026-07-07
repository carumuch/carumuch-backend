CREATE TABLE vehicle (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    license_number VARCHAR(255),
    ownership_type VARCHAR(255),
    brand VARCHAR(255),
    model_year INT,
    model_name VARCHAR(255),
    owner_name VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT uk_vehicle_user_id UNIQUE (user_id),
    CONSTRAINT uk_vehicle_license_number UNIQUE (license_number),
    CONSTRAINT fk_vehicle_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);
