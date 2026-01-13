CREATE TABLE IF NOT EXISTS public.processed_external_message
(
    uuid                UUID                                                                     NOT NULL,
    created_timestamp   timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    processed_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (uuid)
);