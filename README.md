# Restaurant Voting App

* [GitHub repository](https://github.com/sergjei/restaurant_voting)

# Introduction

This is Spring Boot REST Api application for voting for restaurants. It`s created according to test requerements, you
can see below.
Application uses H2 as DataBase and Caffeine Cache.

## Technical requirement:

Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and
couple curl commands to test it (**better - link to Swagger**).

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

-----------------------------

## Project Features

* Non-authentificated users can register themselfes in app
* All other operations available only for authentificated users witn necessary authorization
* Authorization is define by URL(regular user - "/rest/profile", admin - "/rest/admin")
* Meal is subresource of restaurant
* All main errors of app are sent in response body with fields:
  {
  "timestamp": ...,
  "status": ...,
  "error": ...,
  "message": ...
  }

## API Documentation

| HTTP Verbs | Endpoints                                    | Action                                                                                                                |
|------------|----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| GET        | /rest/profile                                | Get current authentificated user                                                                                      |
| PUT        | /rest/profile                                | Update current authentificated user                                                                                   |
| DELETE     | /rest/profile                                | Delete current authentificated user                                                                                   |
| POST       | /rest/profile/register                       | Register new user with role USER. Can`t be accessed by authentificated user                                           |
| GET        | /rest/profile/menu                           | Get list of restaurants with today menu (list of meals)                                                               |
| GET        | /rest/profile/votes                          | User get list of his votes for specified interval of time(inclusive)                                                  |
| POST       | /rest/profile/votes                          | User vote for the restaurant, where wants to have lunch. He can vote only once a day                                  |
| PUT        | /rest/profile/votes/{id}                     | User can change his vote until it is before 11:00                                                                     |
| GET        | /rest/admin/users                            | Get list of all users                                                                                                 |
| POST       | /rest/admin/users                            | Add new user. Can create with role ADMIN                                                                              |
| GET        | /rest/admin/users/{id}                       | Get user by id                                                                                                        |
| PUT        | /rest/admin/users/{id}                       | Update user by id                                                                                                     |
| DELETE     | /rest/admin/users/{id}                       | Delete user by id                                                                                                     |
| GET        | /rest/admin/users/by_email                   | Get user by email                                                                                                     |
| GET        | /rest/admin/votes_count                      | Admin can get vote results (restaurant with amount of voices) for specified period of time. By default - for all time |
| GET        | /rest/admin/votes_count/today                | Admin can get vote results (restaurant with amount of voices) for today                                               |
| GET        | /rest/admin/restaurants/{rest_id}/meals/{id} | Get meal by id                                                                                                        |
| PUT        | /rest/admin/restaurants/{rest_id}/meals/{id} | Update meal by id                                                                                                     |
| DELETE     | /rest/admin/restaurants/{rest_id}/meals/{id} | Delete meal by id                                                                                                     |
| GET        | /rest/admin/restaurants/{rest_id}/meals      | Get list of meals for specified period                                                                                |
| POST       | /rest/admin/restaurants/{rest_id}/meals      | Add single meal to restaurant                                                                                         |                                                                |
| GET        | /rest/admin/restaurants/{id}                 | Get restaurant by id                                                                                                  |
| PUT        | /rest/admin/restaurants/{id}                 | Update restaurant by id                                                                                               |
| DELETE     | /rest/admin/restaurants/{id}                 | Delete restaurant by id                                                                                               |
| GET        | /rest/admin/restaurants                      | Get list of restaurants                                                                                               |
| POST       | /rest/admin/restaurants                      | Create new restaurant                                                                                                 |

You can try app by yourself using Swagger UI with these credentials:

| Role:     | User           | Admin           |
|-----------|----------------|-----------------|
| Login:    | user@gmail.com | admin@gmail.com |
| Password: | password       | admin           |

*[SWAGGER LINK](http://localhost:8080/swagger-ui/index.html)

