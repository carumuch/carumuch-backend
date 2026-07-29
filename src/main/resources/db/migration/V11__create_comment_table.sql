CREATE TABLE comment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    user_id BIGINT,
    board_id BIGINT,
    comment_content VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_comment_board
        FOREIGN KEY (board_id) REFERENCES board (id)
);
