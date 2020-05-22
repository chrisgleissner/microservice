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

| Feature              | [Spring Boot](https://github.com/spring-projects/spring-boot) 2.3.0 | [Quarkus](https://github.com/quarkusio/quarkus) 1.4.2.Final | [Wildfly](https://github.com/wildfly/wildfly) 18.0.1.Final |
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

## JMeter

First start service as described above, then run a JMeter benchmark against it using:
```
mvn -f jmeter/pom.xml clean install -Pjmeter
```

or using a custom configuration of up to 500 threads, ramped up in linear fashion using 5 steps over a duration of 60 seconds:
```
mvn -f jmeter/pom.xml clean install -Pjmeter -Djmeter.targetConcurrency=500 -Djmeter.rampUpSteps=5 -Djmeter.rampUpTime=60 
```

Then open the HTML results at [jmeter/target/jmeter/reports/test/index.html](file://jmeter/target/jmeter/reports/test/index.html).

To edit the test plan, run
```
mvn -f jmeter/pom.xml jmeter:configure jmeter:gui -Pjmeter
```
and open `jmeter/src/test/jmeter/test.jmx`.

## Flame Graphs

see https://queue.acm.org/detail.cfm?id=2927301

### Async Profiler

Use Async Profiler to create Flame Graphs for Java:
* Install https://github.com/jvm-profiling-tools/async-profiler
* Allow collection of performance events by non-root users and resolve kernel symbols properly:
```
sudo sh -c 'echo kernel.perf_event_paranoid=1 >> /etc/sysctl.d/99-perf.conf'
sudo sh -c 'echo kernel.kptr_restrict=0 >> /etc/sysctl.d/99-perf.conf'
sudo sh -c 'sysctl --system'
```

Profile container from without:
* For execution within Docker, download https://github.com/moby/moby/blob/master/profiles/seccomp/default.json, save
as `seccomp-perf.json` and add `perf_event_open` to its `syscalls/names` section, then start Docker container:
```
docker run --security-opt seccomp=seccomp-perf.json
```
* Also see https://github.com/jvm-profiling-tools/async-profiler/blob/master/README.md#profiling-java-in-a-container

Profile container from within:
```
docker ps | grep java
docker exec -it <id> /bin/bash
apt-get update && apt-get install -y wget && wget -c https://github.com/jvm-profiling-tools/async-profiler/releases/download/v1.7/async-profiler-1.7-linux-x64.tar.gz -O - | tar -xz
./profiler.sh -d 30 -f flames.svg -i 999us 1
```

Example for a 30s flame graph recording of process ID `123`, sampling every 999 micros:
```
./profiler.sh -d 30 -f flames.svg -i 999us 123
```
