CREATE TABLE damage_report (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    preferred_repair_sido VARCHAR(100),
    preferred_repair_sigungu VARCHAR(100),
    description VARCHAR(300),
    is_pickup_required TINYINT(1) NOT NULL,
    image_path VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    vehicle_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_damage_report_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);
