# People meals 
`by João Marcelo Renault`

## Setup
```docker
> To start mysql container
docker-compose up

> To start mysql container detached
docker-compose up -d
```
## Usage
```html
> With a browser, access: 
http://localhost:8088/
```
**Note:** It will be shared a dump of the database on mysql folder, on that dump you can create a dump of the database if you want to, and it will be loaded when the DB is loaded.

## Database schema plan
Following relationships were implemented:

[//]: # (<img src="database_schema.png" width=75% height=75%>)
![img_1.png](img_1.png)

## Architecture

It was organized a service-oriented architecture as can be seen below

![img.png](img.png)

## REST APIs

* Dish Entity

`get a Dish entity from repository`
get: /api/v1/dishes/{dishUuid}

`get all Dishes entity from repository`
get: /api/v1/dishes/

`add a Dish entity to repository`
post: /api/v1/dishes/add (DishDTO body)

`remove a Dish entity from repository`
delete: /api/v1/dishes/{dishUuid}

`update a Dish entity from repository`
update: /api/v1/dishes/{dishUuid} (DishDTO body)

* Person Entity

`get a Person entity from repository`
get: /api/v1/persons/{personUuid}

`get all Persones entity from repository`
get: /api/v1/persons/

`add a Person entity to repository`
post: /api/v1/persons/add (PersonDTO body)

`remove a Person entity from repository`
delete: /api/v1/persons/{personUuid}

`update a Person entity from repository`
update: /api/v1/persons/{personUuid} (PersonDTO body)

* Restaurant Entity

`get a Restaurant entity from repository`
get: /api/v1/restaurants/{restaurantUuid}

`get all Restaurantes entity from repository`
get: /api/v1/restaurants/

`add a Restaurant entity to repository`
post: /api/v1/restaurants/add (RestaurantDTO body)

`remove a Restaurant entity from repository`
delete: /api/v1/restaurants/{restaurantUuid}

`update a Restaurant entity from repository`
update: /api/v1/restaurants/{restaurantUuid} (RestaurantDTO body)

* Planning Entity

`get a Planning entity from repository`
get: /api/v1/plannings/{restaurantUuid}

`get all Planning entity from repository`
get: /api/v1/plannings/

`create a planning by associating Dish, Person, Restaurant and DayOfWeek`
post: /api/v1/plannings/ (AssociateForm body)

`remove an existing Planning (previous association)`
remove: /api/v1/plannings/ (AssociateForm body)

`get a List of Persons in a specific Restaurant and DayOfWeek`
get: /api/v1/plannings/restaurant/{restaurantUuid}/{dayOfWeek}

`get a List of Persons with a specific Dish and DayOfWeek`
get: /api/v1/plannings/dish/{dishUuid}/{dayOfWeek}

`get a List of Persons that have no Dish in a DayOfWeek`
get: /api/v1/plannings/no_dish/{dayOfWeek}

## Testing
* There are Unit Tests for each method
* Postman collection for testing:
>https://www.postman.com/jucron/workspace/peoplemeals-endpoints-test-joo-renault/overview

## Work yet to be implemented 
* Documentation
* Integration tests
* Queries for repository use (reducing code amount - streams)
* Others (please suggest)