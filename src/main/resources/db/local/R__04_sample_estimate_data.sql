INSERT INTO estimate (
    id,
    create_date,
    ai_estimated_repair_cost,
    status,
    applicant_count,
    image_path,
    damage_report_id,
    user_id
) VALUES
    (1, '2026-07-04 12:00:00', 820000, 'OPEN', 3, 'https://example.com/images/estimate-1.jpg', 1, 1),
    (2, '2026-07-04 12:01:00', 690000, 'OPEN', 2, 'https://example.com/images/estimate-2.jpg', 2, 2),
    (3, '2026-07-04 12:02:00', 1450000, 'PRIVATE', 1, 'https://example.com/images/estimate-3.jpg', 3, 3),
    (4, '2026-07-04 12:03:00', 730000, 'OPEN', 4, 'https://example.com/images/estimate-4.jpg', 4, 4),
    (5, '2026-07-04 12:04:00', 980000, 'CLOSED', 5, 'https://example.com/images/estimate-5.jpg', 5, 5),
    (6, '2026-07-04 12:05:00', 1580000, 'OPEN', 2, 'https://example.com/images/estimate-6.jpg', 6, 6),
    (7, '2026-07-04 12:06:00', 540000, 'OPEN', 1, 'https://example.com/images/estimate-7.jpg', 7, 7),
    (8, '2026-07-04 12:07:00', 870000, 'PRIVATE', 2, 'https://example.com/images/estimate-8.jpg', 8, 8),
    (9, '2026-07-04 12:08:00', 1120000, 'OPEN', 3, 'https://example.com/images/estimate-9.jpg', 9, 9),
    (10, '2026-07-04 12:09:00', 760000, 'OPEN', 2, 'https://example.com/images/estimate-10.jpg', 10, 10)
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    ai_estimated_repair_cost = VALUES(ai_estimated_repair_cost),
    status = VALUES(status),
    applicant_count = VALUES(applicant_count),
    image_path = VALUES(image_path),
    damage_report_id = VALUES(damage_report_id),
    user_id = VALUES(user_id);

INSERT INTO estimate_repair_parts (
    estimate_id,
    part_name
) VALUES
    (1, '앞범퍼'),
    (1, '조수석 펜더'),
    (2, '후범퍼'),
    (2, '트렁크 패널'),
    (3, '운전석 도어'),
    (3, '사이드 몰딩'),
    (4, '앞범퍼 하단'),
    (4, '안개등 커버'),
    (5, '조수석 뒤문'),
    (5, '휠 하우스'),
    (6, '프론트 범퍼'),
    (6, '우측 헤드램프'),
    (7, '본넷'),
    (7, '전면 그릴'),
    (8, '후측면 범퍼'),
    (8, '쿼터 패널'),
    (9, '슬라이딩 도어'),
    (9, '후륜 펜더'),
    (10, '앞도어'),
    (10, '사이드미러')
ON DUPLICATE KEY UPDATE
    part_name = VALUES(part_name);

INSERT INTO bid (
    id,
    create_date,
    cost,
    repair_method,
    status,
    body_shop_id,
    estimate_id
) VALUES
    (1, '2026-07-04 12:20:00', 790000, '부분 도색 및 판금 수리', 'WAITING', 1, 1),
    (2, '2026-07-04 12:21:00', 670000, '후범퍼 교정 및 트렁크 라인 조정', 'WAITING', 2, 2),
    (3, '2026-07-04 12:22:00', 1380000, '도어 판금 후 전체 도색', 'ACCEPTED', 3, 3),
    (4, '2026-07-04 12:23:00', 700000, '범퍼 복원 및 부품 교체', 'WAITING', 4, 4),
    (5, '2026-07-04 12:24:00', 940000, '문짝 판금 및 도장 재작업', 'ACCEPTED', 5, 5),
    (6, '2026-07-04 12:25:00', 1490000, '헤드램프 교체 포함 외장 복원', 'WAITING', 6, 6),
    (7, '2026-07-04 12:26:00', 510000, '본넷 교정과 그릴 복원', 'REJECTED', 7, 7),
    (8, '2026-07-04 12:27:00', 840000, '후측면 판금과 범퍼 도장', 'WAITING', 8, 8),
    (9, '2026-07-04 12:28:00', 1050000, '도어 판금 및 휀더 도장', 'WAITING', 9, 9),
    (10, '2026-07-04 12:29:00', 730000, '도어 외장 교정과 미러 교체', 'WAITING', 10, 10)
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    cost = VALUES(cost),
    repair_method = VALUES(repair_method),
    status = VALUES(status),
    body_shop_id = VALUES(body_shop_id),
    estimate_id = VALUES(estimate_id);
