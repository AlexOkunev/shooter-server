CREATE TABLE IF NOT EXISTS public.ref_currency
(
    id            integer      NOT NULL,
    enabled       boolean      NOT NULL DEFAULT true,
    name          varchar(255) NOT NULL,
    can_be_bought boolean      NOT NULL DEFAULT true,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.ref_equipment
(
    equipment_id   integer      NOT NULL,
    equipment_type integer      NOT NULL,
    enabled        boolean      NOT NULL DEFAULT true,
    name           varchar(255) NOT NULL,

    PRIMARY KEY (equipment_type, equipment_id)
);

CREATE TABLE IF NOT EXISTS public.player_account
(
    player_id         integer                                                                  NOT NULL,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (player_id)
);

CREATE SEQUENCE IF NOT EXISTS public.seq_player_account_item
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.player_account_item
(
    id              integer NOT NULL DEFAULT nextval('seq_player_account_item'),
    player_id       integer NOT NULL,
    currency_amount integer NOT NULL DEFAULT 0,
    currency_id     integer NOT NULL,
    version         integer NOT NULL default 0,

    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_player_account_item_player_id_currency_id
    ON public.player_account_item (player_id, currency_id);

CREATE TABLE IF NOT EXISTS public.initial_player_account_item
(
    currency_id       integer NOT NULL,
    amount            integer NOT NULL         default 0,
    enabled           boolean NOT NULL         DEFAULT true,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    version           integer NOT NULL         default 0,

    PRIMARY KEY (currency_id)
);

CREATE SEQUENCE IF NOT EXISTS public.seq_money_bundle
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.money_bundle
(
    id                integer NOT NULL         DEFAULT nextval('seq_money_bundle'),
    enabled           boolean NOT NULL         DEFAULT true,
    currency_id       integer NOT NULL,
    currency_amount   integer NOT NULL,
    rubles_price      integer NOT NULL,
    version           integer NOT NULL         default 0,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_money_bundle_currency_id
    ON public.money_bundle (currency_id);

CREATE SEQUENCE IF NOT EXISTS public.seq_product
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.product
(
    id                    integer NOT NULL         DEFAULT nextval('seq_product'),
    enabled               boolean NOT NULL         DEFAULT true,
    ref_equipment_type    integer NOT NULL,
    ref_equipment_id      integer NOT NULL,
    equipment_amount      integer NOT NULL,
    price_ref_currency_id integer NOT NULL,
    price                 integer NOT NULL,
    version               integer NOT NULL         default 0,
    created_timestamp     timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp     timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_product_ref_equipment_type_ref_equipment_id
    ON public.product (ref_equipment_type, ref_equipment_id);

CREATE SEQUENCE IF NOT EXISTS public.seq_money_bundle_trade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.money_bundle_trade
(
    id                integer NOT NULL         DEFAULT nextval('seq_money_bundle_trade'),
    player_id         integer NOT NULL,
    money_bundle_id   integer NOT NULL,
    currency_id       integer NOT NULL,
    currency_amount   integer NOT NULL,
    rubles_price      integer NOT NULL,
    version           integer NOT NULL         default 0,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    status_code       integer NOT NULL,
    uuid              UUID    NOT NULL         DEFAULT gen_random_uuid(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_money_bundle_trade_money_bundle_id
    ON public.money_bundle_trade (money_bundle_id);

CREATE INDEX IF NOT EXISTS idx_money_bundle_trade_player_id
    ON public.money_bundle_trade (player_id);

CREATE SEQUENCE IF NOT EXISTS public.seq_product_trade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.product_trade
(
    id                     integer NOT NULL         DEFAULT nextval('seq_money_bundle_trade'),
    player_id              integer NOT NULL,
    product_id             integer NOT NULL,
    product_equipment_id   integer NOT NULL,
    product_equipment_type integer NOT NULL,
    equipment_amount       integer NOT NULL,
    price_currency_id      integer NOT NULL,
    price_value            integer NOT NULL,
    version                integer NOT NULL         default 0,
    created_timestamp      timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp      timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    status_code            integer NOT NULL,
    uuid                   UUID    NOT NULL         DEFAULT gen_random_uuid(),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_product_trade_product_id
    ON public.product_trade (product_id);

CREATE INDEX IF NOT EXISTS idx_product_trade_equipment
    ON public.product_trade (product_equipment_type, product_equipment_id);

CREATE INDEX IF NOT EXISTS idx_product_trade_player_id
    ON public.product_trade (player_id);