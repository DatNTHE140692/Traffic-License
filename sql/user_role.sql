CREATE TABLE `user_role` (
    `user_id` INT DEFAULT NULL,
    `role_id` INT DEFAULT NULL,
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;