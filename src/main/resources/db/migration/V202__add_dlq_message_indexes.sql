CREATE INDEX idx_dlq_message_status_next_attempt_at
    ON dlq_message (status, next_attempt_at);
