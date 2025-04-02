CREATE SEQUENCE IF NOT EXISTS public.seq_attachment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.attachment
(
    id                               integer      NOT NULL    DEFAULT nextval('seq_attachment'),
    enabled                          boolean      NOT NULL    DEFAULT true,
    name                             varchar(255) NOT NULL,
    type                             integer      NOT NULL    DEFAULT 0,
    effect_sound_loudness_rate       integer      NOT NULL    DEFAULT 0,
    effect_max_zoom_rate             integer      NOT NULL    DEFAULT 0,
    effect_blow_back_rate            integer      NOT NULL    DEFAULT 0,
    effect_laser_max_distance_meters integer      NOT NULL    DEFAULT 0,
    effect_bullet_speed_rate         integer      NOT NULL    DEFAULT 0,
    created_timestamp                timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp                timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_attachment_name ON public.attachment (lower(name) text_pattern_ops);

CREATE TABLE IF NOT EXISTS public.attachment_compatible_gun
(
    attachment_id integer NOT NULL,
    gun_id        integer NOT NULL,

    PRIMARY KEY (attachment_id, gun_id),
    CONSTRAINT fk_attachment FOREIGN KEY (attachment_id) REFERENCES public.attachment (id),
    CONSTRAINT fk_gun FOREIGN KEY (gun_id) REFERENCES public.gun (id)
);
