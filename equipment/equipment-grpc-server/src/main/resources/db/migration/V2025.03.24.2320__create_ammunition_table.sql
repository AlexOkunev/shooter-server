CREATE SEQUENCE IF NOT EXISTS public.seq_ammunition
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.ammunition
(
    id                integer      NOT NULL    DEFAULT nextval('seq_ammunition'),
    enabled           boolean      NOT NULL    DEFAULT true,
    name              varchar(255) NOT NULL,
    speed             integer      NOT NULL    DEFAULT 0,
    damage_mean_value integer      NOT NULL    DEFAULT 0,
    damage_variance   integer      NOT NULL    DEFAULT 0,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_ammunition_name ON public.ammunition (lower(name) text_pattern_ops);

CREATE TABLE IF NOT EXISTS public.ammunition_compatible_gun
(
    ammunition_id integer NOT NULL,
    gun_id        integer NOT NULL,

    PRIMARY KEY (ammunition_id, gun_id),
    CONSTRAINT fk_ammunition FOREIGN KEY (ammunition_id) REFERENCES public.ammunition (id),
    CONSTRAINT fk_gun FOREIGN KEY (gun_id) REFERENCES public.gun (id)
);
