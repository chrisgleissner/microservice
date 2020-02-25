# JVM Microservice Examples

[![Build Status](https://travis-ci.com/chrisgleissner/microservice.svg?branch=master)](https://travis-ci.com/chrisgleissner/microservice)

This repository contains simple REST-based Java 11 microservices for Quarkus, Spring Boot, and Wildfly. 

The aim is to provide the following features for all examined services:
* REST API for `company` and `employee` entities
* DB schema generation
* Security
* Unit and integration tests
* Docker integration

Implemented so far:


| Feature              | [Spring Boot](https://github.com/spring-projects/spring-boot) | [Quarkus](https://github.com/quarkusio/quarkus) | [Wildfly](https://github.com/wildfly/wildfly) |
|----------------------|---------------------------|--------------|---------------|
| Ping REST API        | Yes (Spring Web)          | Yes (JAX-RS) | Yes (JAX-RS)  |
| Company REST API     | Yes (Spring Data REST)    |              |               |
| Employee REST API    | Yes (Spring Web)          | Yes (JAX-RS) |               |
| DB schema generation | Yes (Flyway)              | Yes (Flyway) |               |
| Security             |                           |              |               |
| Unit Tests           | Yes                       | Yes          |               |
| Integration Tests    | Yes                       | Yes          | Yes           |
| Docker               |                           |              | Yes           |

