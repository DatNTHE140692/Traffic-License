CREATE TABLE `province`
(
    `id`      INT auto_increment,
    `name`    VARCHAR (255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `city_id` INT DEFAULT NULL,
    UNIQUE KEY (`name`),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;