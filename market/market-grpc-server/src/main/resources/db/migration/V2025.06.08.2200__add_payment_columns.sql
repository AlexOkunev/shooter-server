ALTER TABLE public.money_bundle_trade
    ADD COLUMN payment_session VARCHAR(50);

ALTER TABLE public.money_bundle_trade
    ADD COLUMN payment_public_token VARCHAR(50);

ALTER TABLE public.money_bundle_trade
    ADD COLUMN payment_uuid UUID;

ALTER TABLE public.money_bundle_trade
    ADD COLUMN payment_start_timestamp timestamp with time zone;

ALTER TABLE public.money_bundle_trade
    ADD COLUMN payment_finish_timestamp timestamp with time zone;