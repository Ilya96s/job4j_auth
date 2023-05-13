# RESTful API архитектура
RestFul API архитектура - это архитектура клиент-серверного приложения, базирующаяся на 6 принципах.

1. Универсальный интерфейс взаимодействия. (Uniform Interface)
2. Запросы без состояния. (Stateless)
3. Поддержка кеширования. (Cacheable)
4. Клиент-серверная ориентация.
5. Поддержка слоев (Layered System)
6. Возможность выполнять код на стороне клиента (Code on Demand)
# Стек технологий
* Java 17
* Maven 3.8.1
* Spring-Boot 2.7.11
* Spring-Boot Data JPA 2.7.11
* Spring-Security 2.7.11
* PostgreSQL 42.3.8
* JWT 3.4.0
* Lombok 1.18.26
* H2DB 2.1.214
* JUnit 5.8.2

# Требования к окружению
* Java 17
* Maven 3.8.1
* PostgreSQL 14

# Запуск проекта
1. В PostgreSQL создать базу данных fullstack_auth
```shell
jdbc:postgresql://127.0.0.1:5432/fullstack_auth
```
2. Запустить проект
```shell
mvn spring-boot run
```
3. Использовать Postman или другой API для выполнения запросов
```shell
http://localhost:8080/person
```
# Контакты для связи
Telegram: ilya96s