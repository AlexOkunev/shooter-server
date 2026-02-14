CREATE TABLE IF NOT EXISTS player_account_log_entry
(
    player_uuid uuid NOT NULL,
    uuid uuid NOT NULL,
    operation_uuid uuid NULL,
    currency_id integer NOT NULL,
    operation_type integer NULL,
    amount_before integer NOT NULL CHECK (amount_before >= 0),
    amount_after integer NOT NULL CHECK (amount_after >= 0),
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    CONSTRAINT pk_player_account_log_entry PRIMARY KEY (player_uuid, uuid)
);

CREATE INDEX IF NOT EXISTS idx_player_account_log_entry_player_created_ts
    ON player_account_log_entry (player_uuid, created_timestamp DESC);
