INSERT INTO vehicle (
    id,
    create_date,
    license_number,
    ownership_type,
    brand,
    model_year,
    model_name,
    owner_name,
    user_id
) VALUES
    (1, '2026-07-04 11:00:00', '101가1001', 'PERSONAL', 'HYUNDAI', 2022, 'SONATA', '김민준', 1),
    (2, '2026-07-04 11:01:00', '102나1002', 'PERSONAL', 'KIA', 2021, 'K5', '이서연', 2),
    (3, '2026-07-04 11:02:00', '103다1003', 'LEASE', 'GENESIS', 2023, 'G80', '박지후', 3),
    (4, '2026-07-04 11:03:00', '104라1004', 'CORPORATE', 'HYUNDAI', 2020, 'AVANTE', '최지민', 4),
    (5, '2026-07-04 11:04:00', '105마1005', 'PERSONAL', 'KIA', 2024, 'SORENTO', '정하은', 5),
    (6, '2026-07-04 11:05:00', '106바1006', 'LEASE', 'TESLA', 2023, 'MODEL 3', '조도윤', 6),
    (7, '2026-07-04 11:06:00', '107사1007', 'PERSONAL', 'BMW', 2022, '320I', '윤서진', 7),
    (8, '2026-07-04 11:07:00', '108아1008', 'PERSONAL', 'MERCEDES-BENZ', 2021, 'E250', '한예린', 8),
    (9, '2026-07-04 11:08:00', '109자1009', 'CORPORATE', 'KIA', 2020, 'CARNIVAL', '오현우', 9),
    (10, '2026-07-04 11:09:00', '110차1010', 'PERSONAL', 'HYUNDAI', 2024, 'TUCSON', '신아린', 10)
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    license_number = VALUES(license_number),
    ownership_type = VALUES(ownership_type),
    brand = VALUES(brand),
    model_year = VALUES(model_year),
    model_name = VALUES(model_name),
    owner_name = VALUES(owner_name),
    user_id = VALUES(user_id);

INSERT INTO damage_report (
    id,
    create_date,
    preferred_repair_sido,
    preferred_repair_sigungu,
    description,
    is_pickup_required,
    image_path,
    status,
    vehicle_id,
    user_id
) VALUES
    (1, '2026-07-04 11:20:00', '서울특별시', '강남구', '앞범퍼 스크래치와 조수석 펜더 경미 손상', 1, 'https://example.com/images/damage-report-1.jpg', 'ANALYZED', 1, 1),
    (2, '2026-07-04 11:21:00', '서울특별시', '송파구', '후범퍼 긁힘과 트렁크 문 단차 발생', 0, 'https://example.com/images/damage-report-2.jpg', 'ANALYZED', 2, 2),
    (3, '2026-07-04 11:22:00', '경기도', '성남시 분당구', '운전석 도어 찌그러짐과 도색 벗겨짐', 1, 'https://example.com/images/damage-report-3.jpg', 'ANALYZED', 3, 3),
    (4, '2026-07-04 11:23:00', '경기도', '고양시 일산동구', '앞범퍼 하단 파손 및 안개등 주변 손상', 0, 'https://example.com/images/damage-report-4.jpg', 'ANALYZED', 4, 4),
    (5, '2026-07-04 11:24:00', '경기도', '수원시 영통구', '조수석 뒤문 스크래치와 휠 하우스 찌그러짐', 1, 'https://example.com/images/damage-report-5.jpg', 'ANALYZED', 5, 5),
    (6, '2026-07-04 11:25:00', '인천광역시', '연수구', '프론트 범퍼와 헤드램프 주변 균열', 1, 'https://example.com/images/damage-report-6.jpg', 'ANALYZED', 6, 6),
    (7, '2026-07-04 11:26:00', '경기도', '안양시 동안구', '본넷 눌림과 그릴 일부 파손', 0, 'https://example.com/images/damage-report-7.jpg', 'ANALYZED', 7, 7),
    (8, '2026-07-04 11:27:00', '경기도', '수원시 영통구', '후측면 범퍼 찌그러짐과 도장 손상', 1, 'https://example.com/images/damage-report-8.jpg', 'ANALYZED', 8, 8),
    (9, '2026-07-04 11:28:00', '경기도', '하남시', '슬라이딩 도어 스크래치와 휀더 미세 변형', 0, 'https://example.com/images/damage-report-9.jpg', 'ANALYZED', 9, 9),
    (10, '2026-07-04 11:29:00', '경기도', '부천시 원미구', '앞도어와 사이드미러 외장 손상', 1, 'https://example.com/images/damage-report-10.jpg', 'ANALYZED', 10, 10)
ON DUPLICATE KEY UPDATE
    create_date = VALUES(create_date),
    preferred_repair_sido = VALUES(preferred_repair_sido),
    preferred_repair_sigungu = VALUES(preferred_repair_sigungu),
    description = VALUES(description),
    is_pickup_required = VALUES(is_pickup_required),
    image_path = VALUES(image_path),
    status = VALUES(status),
    vehicle_id = VALUES(vehicle_id),
    user_id = VALUES(user_id);
