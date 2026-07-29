CREATE TABLE board_image (
    id BIGINT NOT NULL AUTO_INCREMENT,
    create_date DATETIME(6),
    board_id BIGINT,
    original_image_name VARCHAR(255),
    saved_image_name VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_board_image_board
        FOREIGN KEY (board_id) REFERENCES board (id)
);
