CREATE TABLE estimate (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    ai_estimated_repair_cost INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    applicant_count INT NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    damage_report_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estimate_damage_report_id UNIQUE (damage_report_id),
    CONSTRAINT fk_estimate_damage_report
        FOREIGN KEY (damage_report_id) REFERENCES damage_report (id)
);
