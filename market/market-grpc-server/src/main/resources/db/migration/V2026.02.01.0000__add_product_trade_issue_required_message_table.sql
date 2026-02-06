CREATE TABLE IF NOT EXISTS public.product_trade_issue_required_message_outbox
(
    player_uuid       uuid                     NOT NULL,
    message_uuid      uuid                     NOT NULL DEFAULT gen_random_uuid(),
    trade_uuid        uuid                     NOT NULL,
    equipment_id      integer                  NOT NULL,
    equipment_type    integer                  NOT NULL,
    equipment_amount  integer                  NOT NULL,
    created_timestamp timestamp with time zone NOT NULL DEFAULT now(),

    PRIMARY KEY (player_uuid, message_uuid)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_product_trade_issue_required_message_outbox_player_trade
    ON public.product_trade_issue_required_message_outbox (player_uuid, trade_uuid);

CREATE INDEX IF NOT EXISTS idx_product_trade_issue_required_message_outbox_player_time
    ON public.product_trade_issue_required_message_outbox (player_uuid, created_timestamp);
