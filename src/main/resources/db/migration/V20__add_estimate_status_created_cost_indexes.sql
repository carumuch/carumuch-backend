CREATE INDEX idx_estimate_status_created_cost
    ON estimate (status, create_date DESC, ai_estimated_repair_cost, damage_report_id);