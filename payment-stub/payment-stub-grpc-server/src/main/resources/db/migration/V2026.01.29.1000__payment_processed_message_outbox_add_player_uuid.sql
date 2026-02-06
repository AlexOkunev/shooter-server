ALTER TABLE public.payment_processed_message_outbox
    ADD COLUMN player_uuid uuid;

UPDATE public.payment_processed_message_outbox o
SET player_uuid = p.player_uuid
    FROM public.payment p
WHERE p.payment_uuid = o.payment_uuid;

ALTER TABLE public.payment_processed_message_outbox
    ALTER COLUMN player_uuid SET NOT NULL;

ALTER TABLE public.payment_processed_message_outbox
DROP CONSTRAINT pk_payment_processed_message_outbox;

ALTER TABLE public.payment_processed_message_outbox
    ADD CONSTRAINT pk_payment_processed_message_outbox PRIMARY KEY (player_uuid, uuid);

CREATE INDEX idx_payment_processed_message_outbox_player_uuid
    ON public.payment_processed_message_outbox (player_uuid);
