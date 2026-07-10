CREATE TABLE dlq_message (
    id BIGINT NOT NULL AUTO_INCREMENT,
    original_queue VARCHAR(255) NOT NULL,
    payload_json LONGTEXT NOT NULL,
    status VARCHAR(255) NOT NULL,
    attempt_count INT NOT NULL,
    next_attempt_at DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id)
);
