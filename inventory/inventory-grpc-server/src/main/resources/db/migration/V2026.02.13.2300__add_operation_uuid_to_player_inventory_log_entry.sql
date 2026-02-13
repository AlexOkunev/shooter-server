ALTER TABLE player_inventory_log_entry
    ADD COLUMN IF NOT EXISTS operation_uuid uuid;
