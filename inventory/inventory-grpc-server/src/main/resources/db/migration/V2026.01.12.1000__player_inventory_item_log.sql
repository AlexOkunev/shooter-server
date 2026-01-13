CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE public.player_inventory_log_entry
    ADD COLUMN IF NOT EXISTS id_uuid     uuid NOT NULL DEFAULT gen_random_uuid(),
    ADD COLUMN IF NOT EXISTS player_uuid uuid;

ALTER TABLE public.player_inventory_log_entry
    DROP COLUMN IF EXISTS held_amount_before,
    DROP COLUMN IF EXISTS held_amount_after;

ALTER TABLE public.player_inventory_log_entry
    DROP CONSTRAINT IF EXISTS player_inventory_log_entry_pkey;

ALTER TABLE public.player_inventory_log_entry
    DROP COLUMN IF EXISTS id,
    DROP COLUMN IF EXISTS player_id;

ALTER TABLE public.player_inventory_log_entry
    RENAME COLUMN id_uuid TO uuid;

ALTER TABLE public.player_inventory_log_entry
    ALTER COLUMN player_uuid SET NOT NULL;

ALTER TABLE public.player_inventory_log_entry
    ADD CONSTRAINT player_inventory_log_entry_pkey PRIMARY KEY (player_uuid, uuid);

DROP INDEX IF EXISTS public.idx_player_inventory_log_entry_player_id_timestamp;

CREATE INDEX IF NOT EXISTS idx_player_inventory_log_entry_player_uuid_timestamp
    ON public.player_inventory_log_entry (player_uuid, created_timestamp);

DROP SEQUENCE IF EXISTS public.seq_pl_inv_log_entry;
