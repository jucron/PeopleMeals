CREATE TABLE `person` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `fullname` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `telephone` varchar(255) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `fiscal` varchar(255) NOT NULL
);

CREATE TABLE `restaurant` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `opening_time` date,
  `closing_time` date,
  `staff_rest_day` date,
  `maxNumberOfMealsPerDay` bigint
);

CREATE TABLE `dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `recipe_url` varchar(255)
);

CREATE TABLE `planning` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
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
  `person_id` bigint
);

ALTER TABLE `planning` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `credentials` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);