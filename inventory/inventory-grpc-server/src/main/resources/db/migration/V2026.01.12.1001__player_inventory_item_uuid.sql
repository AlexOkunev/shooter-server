CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE public.player_inventory_item
    ADD COLUMN IF NOT EXISTS player_uuid uuid NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE public.player_inventory_item
    DROP COLUMN IF EXISTS held_amount;

ALTER TABLE public.player_inventory_item
    DROP CONSTRAINT IF EXISTS player_inventory_item_pkey;

ALTER TABLE public.player_inventory_item
    DROP COLUMN IF EXISTS player_id;

ALTER TABLE public.player_inventory_item
    ADD CONSTRAINT player_inventory_item_pkey
        PRIMARY KEY (player_uuid, equipment_type, equipment_id);
