CREATE TABLE `role`
(
    `id`   INT auto_increment,
    `code` VARCHAR(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    UNIQUE KEY (`code`),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;