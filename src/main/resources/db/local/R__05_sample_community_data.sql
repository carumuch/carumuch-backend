INSERT INTO board (
    id,
    create_date,
    user_id,
    board_title,
    board_content,
    board_hits
) VALUES
    (1, '2026-07-04 13:00:00', 1, '강남구 판금 잘하는 곳 추천 부탁드립니다', '강남구 근처에서 범퍼와 펜더 수리를 잘하는 공업사를 찾고 있습니다.', 21),
    (2, '2026-07-04 13:01:00', 2, '후범퍼 스크래치 견적 경험 있으신가요', '주차장 접촉 사고로 후범퍼가 긁혔는데 적정 견적이 궁금합니다.', 14),
    (3, '2026-07-04 13:02:00', 3, '수입차 도어 판금 후기 공유합니다', '분당 쪽 공업사에서 도어 판금을 진행한 후기를 남깁니다.', 31),
    (4, '2026-07-04 13:03:00', 4, '일산 지역 픽업 가능한 공업사 있나요', '차를 맡기기 어려워 픽업 가능한 업체를 찾고 있습니다.', 9),
    (5, '2026-07-04 13:04:00', 5, 'SUV 휠 하우스 수리 시간 질문', '휠 하우스 찌그러짐은 보통 며칠 정도 걸리는지 알고 싶습니다.', 11),
    (6, '2026-07-04 13:05:00', 6, '전기차 범퍼 수리 경험담 공유', '전기차 범퍼 손상 수리 시 주의할 점이 있을까요.', 17),
    (7, '2026-07-04 13:06:00', 7, '본넷 눌림 복원 비용이 궁금합니다', '보험 처리 없이 진행하면 어느 정도 금액인지 문의드립니다.', 13),
    (8, '2026-07-04 13:07:00', 8, '광교에서 후측면 사고 수리받은 후기', '후측면 사고 수리 후 만족도가 높았던 경험을 공유합니다.', 24),
    (9, '2026-07-04 13:08:00', 9, '패밀리카 슬라이딩 도어 정비 조언 부탁드립니다', '슬라이딩 도어 외장 손상으로 수리 업체를 비교 중입니다.', 8),
    (10, '2026-07-04 13:09:00', 10, '사이드미러 교체와 도어 도장 같이 가능한가요', '사이드미러 교체와 도어 도장을 한 번에 맡기고 싶습니다.', 16)
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    user_id = VALUES(user_id),
    board_title = VALUES(board_title),
    board_content = VALUES(board_content),
    board_hits = VALUES(board_hits);

INSERT INTO board_image (
    id,
    create_date,
    board_id,
    original_image_name,
    saved_image_name
) VALUES
    (1, '2026-07-04 13:10:00', 1, 'board-01.jpg', 'https://example.com/images/board-01.jpg'),
    (2, '2026-07-04 13:11:00', 2, 'board-02.jpg', 'https://example.com/images/board-02.jpg'),
    (3, '2026-07-04 13:12:00', 3, 'board-03.jpg', 'https://example.com/images/board-03.jpg'),
    (4, '2026-07-04 13:13:00', 4, 'board-04.jpg', 'https://example.com/images/board-04.jpg'),
    (5, '2026-07-04 13:14:00', 5, 'board-05.jpg', 'https://example.com/images/board-05.jpg'),
    (6, '2026-07-04 13:15:00', 6, 'board-06.jpg', 'https://example.com/images/board-06.jpg'),
    (7, '2026-07-04 13:16:00', 7, 'board-07.jpg', 'https://example.com/images/board-07.jpg'),
    (8, '2026-07-04 13:17:00', 8, 'board-08.jpg', 'https://example.com/images/board-08.jpg'),
    (9, '2026-07-04 13:18:00', 9, 'board-09.jpg', 'https://example.com/images/board-09.jpg'),
    (10, '2026-07-04 13:19:00', 10, 'board-10.jpg', 'https://example.com/images/board-10.jpg')
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    board_id = VALUES(board_id),
    original_image_name = VALUES(original_image_name),
    saved_image_name = VALUES(saved_image_name);

INSERT INTO comment (
    id,
    create_date,
    user_id,
    board_id,
    comment_content
) VALUES
    (1, '2026-07-04 13:20:00', 101, 1, '강남권이면 픽업 가능한 업체부터 비교해보시는 게 좋습니다.'),
    (2, '2026-07-04 13:21:00', 102, 2, '후범퍼 단순 스크래치면 사진 견적부터 받아보셔도 됩니다.'),
    (3, '2026-07-04 13:22:00', 103, 3, '수입차는 부품 수급 일정까지 같이 확인하시는 편이 좋습니다.'),
    (4, '2026-07-04 13:23:00', 104, 4, '일산 지역은 픽업 여부와 대차 가능 여부를 함께 보시면 됩니다.'),
    (5, '2026-07-04 13:24:00', 105, 5, '휠 하우스는 외형보다 내부 손상도 같이 점검하셔야 합니다.'),
    (6, '2026-07-04 13:25:00', 106, 6, '전기차는 센서 재보정 필요 여부를 꼭 확인해보셔야 합니다.'),
    (7, '2026-07-04 13:26:00', 107, 7, '보험 처리 없이 진행하실 거면 두세 곳 정도 비교를 권합니다.'),
    (8, '2026-07-04 13:27:00', 108, 8, '광교 쪽은 후측면 사고 수리 경험 많은 업체가 몇 군데 있습니다.'),
    (9, '2026-07-04 13:28:00', 109, 9, '슬라이딩 도어는 레일 정렬까지 같이 점검하시는 게 좋습니다.'),
    (10, '2026-07-04 13:29:00', 110, 10, '도어 도장과 미러 교체는 같은 일정으로 충분히 묶어서 진행 가능합니다.')
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    user_id = VALUES(user_id),
    board_id = VALUES(board_id),
    comment_content = VALUES(comment_content);
