CREATE TABLE estimate_repair_parts (
    estimate_id BIGINT NOT NULL,
    part_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (estimate_id, part_name),
    CONSTRAINT fk_estimate_repair_parts_estimate
        FOREIGN KEY (estimate_id) REFERENCES estimate (id)
);
