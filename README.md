# JVM Microservice Examples

[![Build Status](https://travis-ci.org/chrisgleissner/microservice.svg?branch=master)](https://travis-ci.org/chrisgleissner/microservice)

This repository contains simple REST-based based microservices running on Java 11 and implemented in the following technologies:
* [Spring Boot](https://github.com/spring-projects/spring-boot) 2.3
* [Thorntail](https://github.com/thorntail/thorntail)
* [Wildfly](https://github.com/wildfly/wildfly)

The aim is to provide the following features for all examined services:
* REST API for `company` and `employee` entities
* DB schema generation
* Security
* Unit and integration tests

Implemented so far:

| Feature              | Spring Boot               | Thorntail | Wildfly       |
|----------------------|---------------------------|-----------|---------------|
| Ping REST API        |               |           |           | Yes (JAX-RS)  |
| Company REST API     | Yes (Spring Data REST)    |           |               |
| User REST API        | Yes (Spring Web)          |           |               |
| DB schema generation | Yes (Flyway)              |           |               |
| Security             | No                        |           |               |
| Unit Tests           | Yes (JUnit 5)             |           |               |
| Integration Tests    | Yes (JUnit 5)             |           | Yes (JUnit 4) |      |

