CREATE INDEX idx_damage_report_sigungu
    ON damage_report (preferred_repair_sigungu);

CREATE INDEX idx_damage_report_sido_sigungu
    ON damage_report (preferred_repair_sido, preferred_repair_sigungu);

CREATE INDEX idx_damage_report_created_date
    ON damage_report (create_date);
