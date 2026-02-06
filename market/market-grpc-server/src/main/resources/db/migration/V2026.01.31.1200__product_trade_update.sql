ALTER TABLE public.product_trade
    ADD COLUMN IF NOT EXISTS player_uuid uuid NOT NULL;

ALTER TABLE public.product_trade
DROP COLUMN IF EXISTS player_id;

ALTER TABLE public.product_trade
DROP CONSTRAINT product_trade_pkey;

ALTER TABLE public.product_trade
DROP COLUMN IF EXISTS id;

ALTER TABLE public.product_trade
    ADD CONSTRAINT product_trade_pkey PRIMARY KEY (player_uuid, uuid);

DROP INDEX IF EXISTS public.idx_product_trade_player_id;

CREATE INDEX IF NOT EXISTS idx_product_trade_player_uuid
    ON public.product_trade (player_uuid);

CREATE INDEX IF NOT EXISTS idx_product_trade_player_uuid_created_timestamp
    ON public.product_trade (player_uuid, created_timestamp);

DROP SEQUENCE IF EXISTS public.seq_product_trade;
