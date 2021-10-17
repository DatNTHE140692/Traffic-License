CREATE TABLE `user`
(
    `id`       int auto_increment,
    `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `password` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
    `enabled`  tinyint                                                 default 1,
    `locked`   tinyint                                                 default 0,
    primary key (`id`),
    unique key (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;