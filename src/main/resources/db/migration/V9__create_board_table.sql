CREATE TABLE board (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    user_id BIGINT,
    board_title VARCHAR(255),
    board_content TEXT,
    board_hits INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_board_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);
