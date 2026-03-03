CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$
    DECLARE
total      int := 10000;  потом 1000000
        batch_size int := 2000;
        i          int := 1;
BEGIN
        WHILE i <= total LOOP
                INSERT INTO public.player
                (keycloak_id, email, created_timestamp, enabled, first_name, last_name, realm_id, login)
SELECT
    gen_random_uuid()::text                                     AS keycloak_id,
    ('load_' || gs::text || '@example.test')                    AS email,
    now() - (random() * interval '30 days')                     AS created_timestamp,
    true                                                        AS enabled,

    (
        (ARRAY[
             'Alex','Max','Ilya','Sergey','Dmitry','Nikita','Pavel','Artem','Denis','Kirill',
         'Anna','Elena','Maria','Olga','Irina','Sofia','Daria','Alina','Polina','Ksenia'
             ])[1 + (gs % 20)]
                            || '-' || lpad((gs % 10000)::text, 4, '0')
                        )                                                           AS first_name,

                    (
                        (ARRAY[
                            'Ivanov','Petrov','Sidorov','Smirnov','Kuznetsov','Popov','Volkov','Fedorov','Morozov','Novikov',
                            'Lebedev','Semenov','Egorov','Pavlov','Kozlov','Stepanov','Nikolaev','Orlov','Andreev','Makarov'
                            ])[1 + ((gs * 7) % 20)]
                            || '-' || lpad(((gs * 13) % 10000)::text, 4, '0')
                        )                                                           AS last_name,

                    'load-realm'                                                AS realm_id,
                    ('load_' || gs::text)                                       AS login
FROM generate_series(i, LEAST(i + batch_size - 1, total)) gs;

i := i + batch_size;
END LOOP;
END $$;

-- проверка
SELECT keycloak_id, login, email, first_name, last_name
FROM public.player
ORDER BY created_timestamp DESC
    LIMIT 20;