CREATE TABLE public.product_trade_issuance_performed_message_outbox
(
    player_uuid       uuid NOT NULL,
    message_uuid      uuid NOT NULL,
    trade_uuid        uuid NOT NULL,
    created_timestamp timestamp with time zone DEFAULT ('now'::text)::timestamp with time zone NOT NULL,

    PRIMARY KEY (player_uuid, message_uuid)
);

CREATE INDEX idx_ptirm_player_trade_uuid
    ON public.product_trade_issuance_performed_message_outbox (player_uuid, trade_uuid);

CREATE INDEX idx_ptirm_player_created_timestamp
    ON public.product_trade_issuance_performed_message_outbox (player_uuid, created_timestamp);
