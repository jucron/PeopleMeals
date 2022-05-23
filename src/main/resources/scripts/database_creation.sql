
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
  `staffrestday` date NOT NULL,
  `disheslist_id` bigint UNIQUE
);

CREATE TABLE `disheslist` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT
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

CREATE TABLE `rt_disheslist_dish` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `disheslist_id` bigint,
  `dish_id` bigint
);

ALTER TABLE `restaurant` ADD FOREIGN KEY (`disheslist_id`) REFERENCES `disheslist` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `rt_disheslist_dish` ADD FOREIGN KEY (`disheslist_id`) REFERENCES `disheslist` (`id`);

ALTER TABLE `rt_disheslist_dish` ADD FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);

ALTER TABLE `planning` ADD FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);
