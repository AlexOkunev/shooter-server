ALTER TABLE public.money_bundle_trade
ADD COLUMN IF NOT EXISTS player_uuid UUID NOT NULL;

ALTER TABLE public.money_bundle_trade
DROP COLUMN player_id;

ALTER TABLE public.money_bundle_trade
DROP CONSTRAINT money_bundle_trade_pkey;

ALTER TABLE public.money_bundle_trade
DROP COLUMN id;

ALTER TABLE public.money_bundle_trade
ADD CONSTRAINT money_bundle_trade_pkey PRIMARY KEY (player_uuid, uuid);

DROP INDEX IF EXISTS  public.idx_money_bundle_trade_player_id;

CREATE INDEX IF NOT EXISTS idx_money_bundle_trade_player_uuid
ON public.money_bundle_trade (player_uuid);

DROP SEQUENCE IF EXISTS public.seq_money_bundle_trade;
