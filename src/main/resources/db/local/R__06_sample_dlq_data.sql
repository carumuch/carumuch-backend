INSERT INTO dlq_message (
    id,
    original_queue,
    payload_json,
    status,
    attempt_count,
    next_attempt_at,
    created_at,
    updated_at
) VALUES
    (1, 'estimate.result.queue', '{"estimateId":1,"reason":"timeout"}', 'PENDING', 1, '2026-07-04 14:10:00', '2026-07-04 14:00:00', '2026-07-04 14:05:00'),
    (2, 'estimate.result.queue', '{"estimateId":2,"reason":"deserialization_error"}', 'PROCESSING', 2, '2026-07-04 14:11:00', '2026-07-04 14:01:00', '2026-07-04 14:06:00'),
    (3, 'estimate.result.queue', '{"estimateId":3,"reason":"missing_field"}', 'PENDING', 3, '2026-07-04 14:12:00', '2026-07-04 14:02:00', '2026-07-04 14:07:00'),
    (4, 'estimate.result.queue', '{"estimateId":4,"reason":"external_api_error"}', 'RESOLVED', 1, '2026-07-04 14:13:00', '2026-07-04 14:03:00', '2026-07-04 14:08:00'),
    (5, 'estimate.result.queue', '{"estimateId":5,"reason":"payload_too_large"}', 'GIVE_UP', 5, '2026-07-04 14:14:00', '2026-07-04 14:04:00', '2026-07-04 14:09:00'),
    (6, 'estimate.result.queue', '{"estimateId":6,"reason":"invalid_status"}', 'PENDING', 2, '2026-07-04 14:15:00', '2026-07-04 14:05:00', '2026-07-04 14:10:00'),
    (7, 'estimate.result.queue', '{"estimateId":7,"reason":"schema_mismatch"}', 'PROCESSING', 1, '2026-07-04 14:16:00', '2026-07-04 14:06:00', '2026-07-04 14:11:00'),
    (8, 'estimate.result.queue', '{"estimateId":8,"reason":"connection_reset"}', 'PENDING', 4, '2026-07-04 14:17:00', '2026-07-04 14:07:00', '2026-07-04 14:12:00'),
    (9, 'estimate.result.queue', '{"estimateId":9,"reason":"consumer_exception"}', 'RESOLVED', 2, '2026-07-04 14:18:00', '2026-07-04 14:08:00', '2026-07-04 14:13:00'),
    (10, 'estimate.result.queue', '{"estimateId":10,"reason":"retry_limit"}', 'PENDING', 3, '2026-07-04 14:19:00', '2026-07-04 14:09:00', '2026-07-04 14:14:00')
ON DUPLICATE KEY UPDATE
    original_queue = VALUES(original_queue),
    payload_json = VALUES(payload_json),
    status = VALUES(status),
    attempt_count = VALUES(attempt_count),
    next_attempt_at = VALUES(next_attempt_at),
    created_at = VALUES(created_at),
    updated_at = VALUES(updated_at);
