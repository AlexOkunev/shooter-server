ALTER TABLE public.player_account
    ADD COLUMN IF NOT EXISTS  email varchar(255);
