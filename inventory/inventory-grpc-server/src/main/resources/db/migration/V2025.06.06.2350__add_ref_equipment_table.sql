CREATE TABLE IF NOT EXISTS public.ref_equipment
(
    equipment_id   integer      NOT NULL,
    equipment_type integer      NOT NULL,
    enabled        boolean      NOT NULL DEFAULT true,
    name           varchar(255) NOT NULL,

    PRIMARY KEY (equipment_type, equipment_id)
);