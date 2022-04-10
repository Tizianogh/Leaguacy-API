CREATE TABLE IF NOT EXISTS `squad`
(
    `uuid_squad`
    BINARY
    PRIMARY
    KEY,
    `created`
    TIMESTAMP,
    `updated`
    TIMESTAMP,
    `squad_name`
    varchar
(
    255
),
    );

INSERT INTO `squad` (`uuid_squad`, `created`, `updated`, `squad_name`)
VALUES ('6f6299ea-6fc6-4bc4-a67d-b471142f545e', null, null, 'Hola');
