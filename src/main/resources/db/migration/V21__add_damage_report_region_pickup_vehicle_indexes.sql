CREATE INDEX idx_dr_region_pickup_vehicle
    ON damage_report (preferred_repair_sido, preferred_repair_sigungu, is_pickup_required, vehicle_id);
