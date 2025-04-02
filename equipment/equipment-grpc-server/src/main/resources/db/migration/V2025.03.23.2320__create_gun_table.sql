CREATE SEQUENCE IF NOT EXISTS public.seq_gun
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.gun
(
    id                      integer      NOT NULL    DEFAULT nextval('seq_gun'),
    enabled                 boolean      NOT NULL    DEFAULT true,
    name                    varchar(255) NOT NULL,
    weight_grams            integer      NOT NULL    DEFAULT 0,
    rate_of_fire_per_minute integer      NOT NULL    DEFAULT 0,
    type                    integer      NOT NULL    DEFAULT 0,
    created_timestamp       timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp       timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_gun_name ON public.gun (lower(name) text_pattern_ops);

CREATE INDEX IF NOT EXISTS idx_gun_type ON public.gun using btree (type);