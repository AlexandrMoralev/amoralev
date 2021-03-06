[![Build Status](https://travis-ci.org/AlexandrMoralev/amoralev.svg?branch=master)](https://travis-ci.org/AlexandrMoralev/amoralev/todo_list)
[![codecov](https://codecov.io/gh/AlexandrMoralev/amoralev/branch/master/graph/badge.svg)](https://codecov.io/gh/AlexandrMoralev/amoralev/todo_list)


## Web приложение - todo list.
В данном приложении реализована регистрация, аутентификация и авторизация пользователей. 
Каждый пользователь имеет возможность добавлять и редактировать задачи, просматривать актуальные и выполненные.

#### Технологии и инструменты:

* **Server side**: Java 11, Tomcat 8.5, Servlet api 4.0, Hibernate 5, Jackson, Junit 5, Stream api   
* **Client side**: Javascript es5, Jquery 3.1, Ajax, Bootstrap 4, HTML, CSS

* **Tools**: Maven, Checkstyle
* **CI/CD**:  Travis ci, Jacoco, Codecov
* **DB**: Postgresql 42.2.5, h2database 1.4.200

#### Скриншоты приложения:

* Страница регистрации и аутентификации
![](/screenshots/loginPage.png)

* Регистрация нового пользователя
![](/screenshots/successfulRegistration.png)

* Ошибка авторизации 
![](/screenshots/authError.png)

* Успешная авторизация 
![](/screenshots/authSuccess.png)

* Просмотр актуальных задач
![](/screenshots/activeTasks.png)

* Добавление новой задачи
![](/screenshots/taskCreation.png)
![](/screenshots/taskCreated.png)

* Просмотр всех задач
![](/screenshots/allTasks.png)
