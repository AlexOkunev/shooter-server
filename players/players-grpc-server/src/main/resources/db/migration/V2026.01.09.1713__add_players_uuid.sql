CREATE EXTENSION IF NOT EXISTS pgcrypto;

ALTER TABLE public.player ADD COLUMN player_uuid uuid;

UPDATE public.player
SET player_uuid = gen_random_uuid()
WHERE player_uuid IS NULL;

ALTER TABLE public.player ALTER COLUMN player_uuid SET NOT NULL;

ALTER TABLE public.player ALTER COLUMN player_uuid SET DEFAULT gen_random_uuid();

ALTER TABLE public.player ADD CONSTRAINT uk_player_uuid UNIQUE (player_uuid);
