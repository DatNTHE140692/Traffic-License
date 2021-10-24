CREATE TABLE `user`
(
    `id`                    int auto_increment,
    `username`              varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `password`              varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `email`                 varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `phone_number`          varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci  DEFAULT NULL,
    `identification_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `district_id`           int                                                     DEFAULT NULL,
    `province_id`           int                                                     DEFAULT NULL,
    `city_id`               int                                                     DEFAULT NULL,
    `company_id`            int                                                     DEFAULT NULL,
    `no_vaccinated`         tinyint                                                 DEFAULT 0,
    `enabled`               tinyint                                                 DEFAULT 1,
    `locked`                tinyint                                                 DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`username`, `phone_number`, `email`, `identification_number`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;