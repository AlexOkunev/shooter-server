CREATE TABLE IF NOT EXISTS public.initial_player_inventory_item
(
    equipment_type    integer NOT NULL,
    equipment_id      integer NOT NULL,
    amount            integer NOT NULL         default 0,
    enabled           boolean NOT NULL         DEFAULT true,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    updated_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,
    version           integer NOT NULL         default 0,

    PRIMARY KEY (equipment_type, equipment_id)
);

CREATE TABLE IF NOT EXISTS public.player_inventory_item
(
    player_id      integer NOT NULL,
    equipment_type integer NOT NULL,
    equipment_id   integer NOT NULL,
    amount         integer NOT NULL default 0,
    held_amount    integer NOT NULL default 0,
    version        integer NOT NULL default 0,

    PRIMARY KEY (player_id, equipment_type, equipment_id)
);

CREATE SEQUENCE IF NOT EXISTS public.seq_pl_inv_log_entry
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.player_inventory_log_entry
(
    id                 integer NOT NULL,
    player_id          integer NOT NULL,
    equipment_type     integer NOT NULL,
    equipment_id       integer NOT NULL,
    operation_type     integer NOT NULL,
    amount_before      integer NOT NULL         default 0,
    amount_after       integer NOT NULL         default 0,
    held_amount_before integer NOT NULL         default 0,
    held_amount_after  integer NOT NULL         default 0,
    created_timestamp  timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_player_inventory_log_entry_player_id_timestamp
    ON public.player_inventory_log_entry (player_id, created_timestamp);