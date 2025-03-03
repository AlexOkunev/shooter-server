CREATE SEQUENCE IF NOT EXISTS public.seq_player
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.player
(
    keycloak_id       varchar(36) NOT NULL,
    player_id         integer     NOT NULL DEFAULT nextval('seq_player'),
    email             varchar(255),
    created_timestamp timestamp with time zone,
    enabled           boolean     NOT NULL DEFAULT FALSE,
    first_name        varchar(255),
    last_name         varchar(255),
    realm_id          varchar(255),
    login             varchar(255),

    PRIMARY KEY (keycloak_id),
    CONSTRAINT uk_player_id UNIQUE (player_id)
);

CREATE INDEX IF NOT EXISTS idx_player_email ON public.player ((lower(email)));

CREATE INDEX IF NOT EXISTS idx_player_login ON public.player ((lower(login)));