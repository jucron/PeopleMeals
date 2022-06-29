# People meals

`by João Marcelo Renault`

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/jucron/PeopleMeals/tree/master.svg?style=svg&circle-token=54174309f349bce553da6585d36efe2725d7905d)](https://dl.circleci.com/status-badge/redirect/gh/jucron/PeopleMeals/tree/master)

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

* Login

[Login documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNMw)

* Dish Entity

[Dish Entity documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNMv)

* Person Entity

[Person Entity documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNMy)

* Restaurant Entity

[Restaurant Entity documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNN2)

* Planning Entity

[Planning Entity documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNN3)

* Credentials Entity

[Credentials Entity documentation here](https://documenter.getpostman.com/view/18570764/UzBvFNHe)

## Testing

* There are Unit Tests for each method
* An Integration Test was implemented for processes that require Database use
* Postman collection for testing:

> [Postman - PeopleMeals endpoints Test](https://www.postman.com/jucron/workspace/peoplemeals-endpoints-test-joo-renault/overview)
