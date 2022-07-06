CREATE TABLE `person` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` binary(16) UNIQUE,
  `fullname` varchar(100) NOT NULL,
  `telephone` varchar(100),
  `mobile` varchar(100),
  `fiscal` varchar(100)
);

CREATE TABLE `restaurant` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` binary(16) UNIQUE,
  `name` varchar(100) NOT NULL,
  `opening_time` varchar(100),
  `closing_time` varchar(100),
  `staff_rest_day` varchar(100),
  `max_meals` bigint
);

CREATE TABLE `dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` binary(16) UNIQUE,
  `name` varchar(100) NOT NULL,
  `recipe_url` varchar(100)
);

CREATE TABLE `planning` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` binary(16) UNIQUE,
  `day_of_week` varchar(10) NOT NULL,
  `dish_id` bigint,
  `person_id` bigint,
  `restaurant_id` bigint
);

CREATE TABLE `rt_restaurant_dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `restaurant_id` bigint,
  `dish_id` bigint
);

CREATE TABLE `credentials` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `deactivation_date` varchar(100),
  `role` varchar(20) NOT NULL,
  `person_id` bigint
);

ALTER TABLE `planning` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `credentials` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);

INSERT INTO credentials (username, password, deactivation_date, role) VALUES ("admin", "$2a$10$vJVJvUfutK.nsYI/QHJbtev.9146JPDYMiXgyYZ2Y8wZvEqyCmQ2m", null, "admin");
INSERT INTO credentials (username, password, deactivation_date, role) VALUES ("user", "$2a$10$BsRE7UwKEictb2KjdqJrL.1X/K8mbKVrKyutbrI45WpAvJafuo1si", null, "user");

ALTER TABLE `person`
ADD `creator_username` varchar(100),
ADD `creator_date` varchar(100),
ADD `last_modifier_username` varchar(100),
ADD `last_modifier_date` varchar(100);

ALTER TABLE `restaurant`
ADD `creator_username` varchar(100),
ADD `creator_date` varchar(100),
ADD `last_modifier_username` varchar(100),
ADD `last_modifier_date` varchar(100);