CREATE TABLE `person` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(100) UNIQUE,
  `fullname` varchar(255) NOT NULL,
  `telephone` varchar(255),
  `mobile` varchar(255),
  `fiscal` varchar(255)
);

CREATE TABLE `restaurant` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(100),
  `name` varchar(255) NOT NULL,
  `opening_time` date,
  `closing_time` date,
  `staff_rest_day` date,
  `max_meals` bigint
);

CREATE TABLE `dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(100),
  `name` varchar(255) NOT NULL,
  `recipe_url` varchar(255)
);

CREATE TABLE `planning` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `uuid` varchar(100),
  `day_of_week` varchar(255) NOT NULL,
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
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `deactivation_date` varchar(255),
  `role` varchar(255) NOT NULL,
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

ALTER TABLE person
ADD `creator_username` varchar(255),
ADD `creator_date` varchar(255),
ADD `last_modifier_username` varchar(255),
ADD `last_modifier_date` varchar(255);

alter table restaurant
add `creator_username` varchar(255),
ADD `creator_date` varchar(255),
ADD `last_modifier_username` varchar(255),
ADD `last_modifier_date` varchar(255);