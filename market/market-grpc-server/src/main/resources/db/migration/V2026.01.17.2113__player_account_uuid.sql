CREATE
EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE public.player_account_item
    ADD COLUMN IF NOT EXISTS player_uuid uuid NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE public.player_account_item
DROP CONSTRAINT IF EXISTS player_account_item_pkey;

ALTER TABLE public.player_account_item
ALTER COLUMN id DROP DEFAULT;

ALTER TABLE public.player_account_item
DROP COLUMN IF EXISTS id,
DROP COLUMN IF EXISTS player_id;

ALTER TABLE public.player_account_item
ALTER COLUMN player_uuid DROP DEFAULT;

ALTER TABLE public.player_account_item
ADD CONSTRAINT player_account_item_pkey PRIMARY KEY (player_uuid, currency_id);

DROP INDEX IF EXISTS public.uk_player_account_item_player_id_currency_id;

DROP SEQUENCE IF EXISTS public.seq_player_account_item;

ALTER TABLE public.player_account
ADD COLUMN IF NOT EXISTS player_uuid uuid NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE public.player_account
DROP CONSTRAINT player_account_pkey;

ALTER TABLE public.player_account
ADD CONSTRAINT player_account_pkey PRIMARY KEY (player_uuid);

ALTER TABLE public.player_account
DROP COLUMN player_id;

