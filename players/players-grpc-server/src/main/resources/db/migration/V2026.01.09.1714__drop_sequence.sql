ALTER TABLE public.player DROP CONSTRAINT uk_player_id;

ALTER TABLE public.player DROP COLUMN player_id;

DROP SEQUENCE public.seq_player;
