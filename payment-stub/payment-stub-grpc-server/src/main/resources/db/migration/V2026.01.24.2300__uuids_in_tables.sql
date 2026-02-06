ALTER TABLE public.payment
    ADD COLUMN player_uuid uuid;

UPDATE public.payment
SET player_uuid = gen_random_uuid()
WHERE player_uuid IS NULL;

ALTER TABLE public.payment
ALTER COLUMN payment_session SET NOT NULL,
ALTER COLUMN public_token SET NOT NULL;

ALTER TABLE public.payment
ALTER COLUMN processing_finished_timestamp DROP NOT NULL,
ALTER COLUMN processing_finished_timestamp DROP DEFAULT;

ALTER TABLE public.payment
DROP CONSTRAINT payment_pkey;

ALTER TABLE public.payment
DROP COLUMN player_id,
DROP COLUMN id;

ALTER TABLE public.payment
ALTER COLUMN player_uuid SET NOT NULL,
ALTER COLUMN payment_uuid SET NOT NULL;

ALTER TABLE public.payment ADD CONSTRAINT pk_payment PRIMARY KEY (player_uuid, payment_uuid);

DROP INDEX public.uk_payment_payment_uuid;

CREATE INDEX idx_payment_payment_uuid ON public.payment (payment_uuid);

DROP SEQUENCE public.seq_payment;

ALTER TABLE public.payment_processed_message_outbox RENAME COLUMN message_uuid TO uuid;

ALTER TABLE public.payment_processed_message_outbox
DROP CONSTRAINT payment_processed_message_outbox_pkey;

ALTER TABLE public.payment_processed_message_outbox
DROP COLUMN id;

ALTER TABLE public.payment_processed_message_outbox
ALTER COLUMN uuid SET NOT NULL,
ALTER COLUMN uuid SET DEFAULT gen_random_uuid();

ALTER TABLE public.payment_processed_message_outbox
ADD CONSTRAINT pk_payment_processed_message_outbox PRIMARY KEY (uuid);

DROP INDEX public.uk_payment_processed_message_outbox_message_uuid;

DROP SEQUENCE public.seq_payment_processed_message;
