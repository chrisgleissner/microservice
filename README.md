# JVM Microservice Examples

[![Build Status](https://travis-ci.com/chrisgleissner/microservice.svg?branch=master)](https://travis-ci.com/chrisgleissner/microservice)

This repository contains simple REST-based Java 11 microservices for Spring Boot, Quarkus and Wildfly. 

The aim is to provide the following features for all services:
* REST API for `company` and `employee` entities
* DB schema generation
* JWT-based Security
* Unit and integration tests
* Docker integration

## Features

Implemented features:

| Feature              | [Spring Boot](https://github.com/spring-projects/spring-boot) 2.3.0.M4 | [Quarkus](https://github.com/quarkusio/quarkus) 1.2.1.Final | [Wildfly](https://github.com/wildfly/wildfly) 18.0.1.Final |
|----------------------|---------------------------|--------------|---------------|
| Ping REST API        | Yes (Spring Web)          | Yes (JAX-RS) | Yes (JAX-RS)  |
| Company REST API     | Yes (Spring Data REST)    |              |               |
| Employee REST API    | Yes (Spring Web)          | Yes (JAX-RS) |               |
| DB schema generation | Yes (Flyway)              | Yes (Flyway) |               |
| JWT Security         | Yes                       | Yes          |               |
| Unit Tests           | Yes                       | Yes          |               |
| Integration Tests    | Yes                       | Yes          | Yes           |
| Swagger              | Yes                       | Yes          | Yes           |
| Docker               | Yes                       |              | Yes           |
| JMeter               | Yes                       |              |               |

# Build

Build with:
```
mvn clean install
```

For creating Docker images, first install a `docker` daemon and then build with:
```
mvn clean install -Pdocker
```

# Run

## Spring Boot

```
mvn -f springboot/pom.xml spring-boot:run
```

or, using Docker:
```
docker run -p8080:8080 microservice-springboot
```

# Performance Test

First start service as described above, then run a JMeter benchmark against it using:
```
mvn -f jmeter/pom.xml install -Pjmeter
```

or using a custom configuration of 50 users and a test duration of 20 seconds:
```
mvn -f jmeter/pom.xml clean install -Pjmeter -Djmeter.users=50  -Djmeter.duration=20 
```

Then open the HTML results at [jmeter/target/jmeter/reports/test/index.html](file://jmeter/target/jmeter/reports/test/index.html).

To edit the test plan, run
```
mvn -f jmeter/pom.xml jmeter:configure jmeter:gui -Pjmeter
```
and open `jmeter/src/test/jmeter/test.jmx`.
