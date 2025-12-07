INSERT INTO country (
    country_code
    ,name
    ,created_at
    ,updated_at
)
values (
        'CH'
        ,'Switzerland'
        , now()
        , now()
       );

-- 기존 Holiday 1건
INSERT INTO holiday (
    holiday_id, created_at, updated_at, date, fixed, global,
    launch_year, local_name, name, holiday_year, country_code
)
VALUES
    (329, '2025-12-07 15:04:21.668488', '2025-12-07 15:04:21.668488',
     '2020-01-06', false, false, null, 'Heilige Drei Könige', 'Epiphany', 2020, 'CH');

-- Scope 2건
INSERT INTO holiday_scope (scope_id, holiday_id)
VALUES
    (10030, 329),
    (10031, 329);

-- Scope County
INSERT INTO holiday_scope_county (scope_id, county_code)
VALUES
    (10030, 'CH-UR'),
    (10030, 'CH-SZ'),
    (10031, 'CH-TI');

-- Scope Type
INSERT INTO holiday_scope_type (scope_id, holiday_type)
VALUES
    (10030, 'OBSERVANCE'),
    (10031, 'PUBLIC');
