CREATE INDEX idx_estimate_status_created_id_cost
    ON estimate (status, create_date DESC, id DESC, ai_estimated_repair_cost, damage_report_id);