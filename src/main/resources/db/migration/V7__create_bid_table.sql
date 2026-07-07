CREATE TABLE bid (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    cost INT NOT NULL,
    repair_method VARCHAR(255),
    status VARCHAR(255),
    body_shop_id BIGINT,
    estimate_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_bid_body_shop
        FOREIGN KEY (body_shop_id) REFERENCES body_shop (id),
    CONSTRAINT fk_bid_estimate
        FOREIGN KEY (estimate_id) REFERENCES estimate (id)
);
