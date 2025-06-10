CREATE SEQUENCE IF NOT EXISTS public.seq_processed_external_message
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.processed_external_message
(
    id                        integer NOT NULL         DEFAULT nextval('seq_processed_external_message'),
    message_uuid              UUID    NOT NULL,
    message_created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_processed_external_message_uuid
    ON public.processed_external_message (message_uuid);