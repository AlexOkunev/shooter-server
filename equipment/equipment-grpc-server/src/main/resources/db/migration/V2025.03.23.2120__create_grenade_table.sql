CREATE SEQUENCE IF NOT EXISTS public.seq_grenade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.grenade
(
    id                         integer      NOT NULL    DEFAULT nextval('seq_grenade'),
    enabled                    boolean      NOT NULL    DEFAULT true,
    name                       varchar(255) NOT NULL,
    blast_damage_radius_meters integer      NOT NULL    DEFAULT 0,
    max_blast_damage_hp        integer      NOT NULL    DEFAULT 0,
    max_blind_time_ms          integer      NOT NULL    DEFAULT 0,
    max_deaf_time_ms           integer      NOT NULL    DEFAULT 0,
    created_timestamp          timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp          timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_grenade_name ON public.grenade (lower(name) text_pattern_ops);