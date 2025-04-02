CREATE SEQUENCE IF NOT EXISTS public.seq_currency
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.currency
(
    id                    integer      NOT NULL    DEFAULT nextval('seq_currency'),
    enabled               boolean      NOT NULL    DEFAULT true,
    name                  varchar(255) NOT NULL,
    can_be_bought         boolean      NOT NULL    DEFAULT true,
    can_be_given_as_award boolean      NOT NULL    DEFAULT true,
    created_timestamp     timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp     timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_currency_name ON public.currency (lower(name) text_pattern_ops);