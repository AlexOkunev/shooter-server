ALTER TABLE public.processed_external_message
    ADD  COLUMN IF NOT EXISTS message_processed_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL;

ALTER TABLE public.processed_external_message
    DROP CONSTRAINT IF EXISTS processed_external_message_pkey;

ALTER TABLE public.processed_external_message
    ADD CONSTRAINT processed_external_message_pkey PRIMARY KEY (message_uuid);

DROP INDEX IF EXISTS public.uk_processed_external_message_uuid;

ALTER TABLE public.processed_external_message
    DROP COLUMN IF EXISTS id;

DROP SEQUENCE IF EXISTS public.seq_processed_external_message;

CREATE INDEX IF NOT EXISTS idx_money_bundle_trade_player_uuid_created_timestamp
    ON public.money_bundle_trade (player_uuid, created_timestamp);
