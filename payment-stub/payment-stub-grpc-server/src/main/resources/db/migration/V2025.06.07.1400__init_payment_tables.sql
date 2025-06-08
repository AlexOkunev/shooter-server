CREATE SEQUENCE IF NOT EXISTS public.seq_payment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.payment
(
    id                            integer      NOT NULL    DEFAULT nextval('seq_payment'),
    player_id                     integer      NOT NULL,
    player_email                  varchar(255) NOT NULL,
    trade_uuid                    UUID         NOT NULL,
    rubles_amount                 integer      NOT NULL,
    created_timestamp             timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    processing_finished_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    payment_uuid                  UUID         NOT NULL    DEFAULT gen_random_uuid(),
    payment_session               varchar(255),
    public_token                  varchar(255),
    status                        integer      NOT NULL    default 0,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_trade_uuid
    ON public.payment (trade_uuid);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_payment_uuid
    ON public.payment (payment_uuid);

CREATE INDEX IF NOT EXISTS idx_payment_processing_finished_timestamp
    ON public.payment (processing_finished_timestamp);

CREATE SEQUENCE IF NOT EXISTS public.seq_payment_processed_message
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.payment_processed_message_outbox
(
    id                            integer                  NOT NULL DEFAULT nextval('seq_payment_processed_message'),
    message_uuid                  UUID                     NOT NULL DEFAULT gen_random_uuid(),
    trade_uuid                    UUID                     NOT NULL,
    payment_uuid                  UUID                     NOT NULL,
    created_timestamp             timestamp with time zone          DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    processing_finished_timestamp timestamp with time zone NOT NULL,
    status                        integer                  NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_processed_message_outbox_trade_uuid
    ON public.payment_processed_message_outbox (trade_uuid);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_processed_message_outbox_payment_uuid
    ON public.payment_processed_message_outbox (payment_uuid);

CREATE UNIQUE INDEX IF NOT EXISTS uk_payment_processed_message_outbox_message_uuid
    ON public.payment_processed_message_outbox (message_uuid);