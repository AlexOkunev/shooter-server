ALTER TABLE public.product_trade_issuance_performed_message_outbox
    ADD COLUMN success boolean DEFAULT true NOT NULL;

CREATE UNIQUE INDEX ux_ptipm_player_trade_uuid_success_true
    ON public.product_trade_issuance_performed_message_outbox (player_uuid, trade_uuid) WHERE success = true;