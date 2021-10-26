CREATE TABLE `company`
(
    `id`           int auto_increment,
    `name`         varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `license_no`   varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `phone_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci  DEFAULT NULL,
    `ward_id`      int                                                     DEFAULT NULL,
    `province_id`  int                                                     DEFAULT NULL,
    `city_id`      int                                                     DEFAULT NULL,
    `active`       tinyint                                                 default 1,
    unique key (`license_no`, `phone_number`),
    primary key (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;