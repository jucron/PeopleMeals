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
  `openingtime` date NOT NULL,
  `closingtime` date NOT NULL,
  `staffrestday` date NOT NULL
);

CREATE TABLE `dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `recipeurl` varchar(255)
);

CREATE TABLE `planning` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `dayofweek` varchar(255) NOT NULL,
  `dish_id` bigint,
  `person_id` bigint
);

CREATE TABLE `rt_restaurant_dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `restaurant_id` bigint,
  `dish_id` bigint
);

ALTER TABLE `planning` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`);

ALTER TABLE `rt_restaurant_dish` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);
