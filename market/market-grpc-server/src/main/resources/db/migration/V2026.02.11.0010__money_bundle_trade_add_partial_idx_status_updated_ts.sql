CREATE INDEX IF NOT EXISTS idx_mbt_st2_3_updated_ts
    ON money_bundle_trade (status_code, updated_timestamp)
    WHERE status_code IN (2, 3); -- PAYMENT_CREATION_WAIT and PAYMENT_PENDING