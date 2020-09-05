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
| Vert.x               | Yes                       |              |               |
| JMeter Benchmark     | Yes                       |              |               |
| ab Benchmark         | Yes                       |              |               |

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

## Benchmark

Comparison of Vert.x, Spring Web, Spring Webflux with varying concurrency (1, 100, 1000 concurrent users).

### Run

To run the benchmarks, first start SpringBoot
```
mvn clean install
cd springboot/bin
./start-service.sh 
```
and then the benchmarks with
```
./benchmark.sh
```

### Results

Config used:
* Metal: Intel 6700K 4.5GHz with Windows 10 64Bit
* VM: Xubuntu 18.04 in Virtualbox 6.1.12
* JDK: OpenJDK 11.0.7 64Bit, SpringBoot started with `-Xmx128m-Xms128m`
* Framework versions: Spring Boot 2.3.0.RELEASE, Vert.x Web 3.9.2

Output from `benchmark.sh`:

#### Concurrency 1
```
##############################################
# Benchmark, concurrency 1
##############################################

==============================================
Spring Web, concurrency 1
http://localhost:8080/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Finished 26696 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      1
Time taken for tests:   5.000 seconds
Complete requests:      26696
Failed requests:        0
Keep-Alive requests:    26430
Total transferred:      4744174 bytes
HTML transferred:       640704 bytes
Requests per second:    5339.10 [#/sec] (mean)
Time per request:       0.187 [ms] (mean)
Time per request:       0.187 [ms] (mean, across all concurrent requests)
Transfer rate:          926.58 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       0
Processing:     0    0   0.3      0      19
Waiting:        0    0   0.3      0      19
Total:          0    0   0.3      0      19

Percentage of the requests served within a certain time (ms)
  50%      0
  66%      0
  75%      0
  80%      0
  90%      0
  95%      0
  98%      0
  99%      0
 100%     19 (longest request)

==============================================
Spring Webflux, concurrency 1
http://localhost:8080/micro/api/ping/flux
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Finished 16781 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping/flux
Document Length:        24 bytes

Concurrency Level:      1
Time taken for tests:   5.001 seconds
Complete requests:      16781
Failed requests:        0
Keep-Alive requests:    16614
Total transferred:      2982175 bytes
HTML transferred:       402744 bytes
Requests per second:    3355.84 [#/sec] (mean)
Time per request:       0.298 [ms] (mean)
Time per request:       0.298 [ms] (mean, across all concurrent requests)
Transfer rate:          582.39 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       0
Processing:     0    0   0.3      0      13
Waiting:        0    0   0.3      0      13
Total:          0    0   0.3      0      13

Percentage of the requests served within a certain time (ms)
  50%      0
  66%      0
  75%      0
  80%      0
  90%      0
  95%      0
  98%      0
  99%      0
 100%     13 (longest request)

==============================================
Vert.x, concurrency 1
http://localhost:8090/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Completed 50000 requests
Finished 50000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8090

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      1
Time taken for tests:   3.068 seconds
Complete requests:      50000
Failed requests:        0
Keep-Alive requests:    50000
Total transferred:      5950000 bytes
HTML transferred:       1200000 bytes
Requests per second:    16297.11 [#/sec] (mean)
Time per request:       0.061 [ms] (mean)
Time per request:       0.061 [ms] (mean, across all concurrent requests)
Transfer rate:          1893.90 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       0
Processing:     0    0   0.1      0      12
Waiting:        0    0   0.1      0      12
Total:          0    0   0.1      0      12

Percentage of the requests served within a certain time (ms)
  50%      0
  66%      0
  75%      0
  80%      0
  90%      0
  95%      0
  98%      0
  99%      0
 100%     12 (longest request)
```

#### Concurrency 100
```
##############################################
# Benchmark, concurrency 100
##############################################

==============================================
Spring Web, concurrency 100
http://localhost:8080/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Completed 50000 requests
Finished 50000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      100
Time taken for tests:   3.588 seconds
Complete requests:      50000
Failed requests:        0
Keep-Alive requests:    49559
Total transferred:      8887211 bytes
HTML transferred:       1200000 bytes
Requests per second:    13935.60 [#/sec] (mean)
Time per request:       7.176 [ms] (mean)
Time per request:       0.072 [ms] (mean, across all concurrent requests)
Transfer rate:          2418.92 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       7
Processing:     0    7   5.1      7      62
Waiting:        0    7   5.1      7      62
Total:          0    7   5.1      7      62

Percentage of the requests served within a certain time (ms)
  50%      7
  66%      8
  75%      9
  80%     10
  90%     13
  95%     17
  98%     21
  99%     24
 100%     62 (longest request)

==============================================
Spring Webflux, concurrency 100
http://localhost:8080/micro/api/ping/flux
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Finished 46307 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping/flux
Document Length:        24 bytes

Concurrency Level:      100
Time taken for tests:   5.004 seconds
Complete requests:      46307
Failed requests:        0
Keep-Alive requests:    45895
Total transferred:      8230698 bytes
HTML transferred:       1111368 bytes
Requests per second:    9254.61 [#/sec] (mean)
Time per request:       10.805 [ms] (mean)
Time per request:       0.108 [ms] (mean, across all concurrent requests)
Transfer rate:          1606.38 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0      12
Processing:     0   11   8.9     10     143
Waiting:        0   11   8.9     10     143
Total:          0   11   8.9     10     143

Percentage of the requests served within a certain time (ms)
  50%     10
  66%     12
  75%     14
  80%     15
  90%     19
  95%     24
  98%     34
  99%     45
 100%    143 (longest request)

==============================================
Vert.x, concurrency 100
http://localhost:8090/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Completed 50000 requests
Finished 50000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8090

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      100
Time taken for tests:   0.773 seconds
Complete requests:      50000
Failed requests:        0
Keep-Alive requests:    50000
Total transferred:      5950000 bytes
HTML transferred:       1200000 bytes
Requests per second:    64707.16 [#/sec] (mean)
Time per request:       1.545 [ms] (mean)
Time per request:       0.015 [ms] (mean, across all concurrent requests)
Transfer rate:          7519.68 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       3
Processing:     0    2   1.1      1      15
Waiting:        0    2   1.1      1      15
Total:          0    2   1.1      1      15

Percentage of the requests served within a certain time (ms)
  50%      1
  66%      1
  75%      2
  80%      2
  90%      2
  95%      2
  98%      3
  99%      5
 100%     15 (longest request)
```

#### Concurrency 1000
```
##############################################
# Benchmark, concurrency 1000
##############################################

==============================================
Spring Web, concurrency 1000
http://localhost:8080/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Completed 50000 requests
Finished 50000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      1000
Time taken for tests:   3.256 seconds
Complete requests:      50000
Failed requests:        0
Keep-Alive requests:    49990
Total transferred:      8899710 bytes
HTML transferred:       1200000 bytes
Requests per second:    15358.52 [#/sec] (mean)
Time per request:       65.110 [ms] (mean)
Time per request:       0.065 [ms] (mean, across all concurrent requests)
Transfer rate:          2669.66 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   3.9      0      34
Processing:     0   64  41.9     60     623
Waiting:        0   64  41.9     60     623
Total:          0   64  42.2     60     623

Percentage of the requests served within a certain time (ms)
  50%     60
  66%     73
  75%     80
  80%     85
  90%    104
  95%    126
  98%    167
  99%    201
 100%    623 (longest request)

==============================================
Spring Webflux, concurrency 1000
http://localhost:8080/micro/api/ping/flux
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Finished 46560 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8080

Document Path:          /micro/api/ping/flux
Document Length:        24 bytes

Concurrency Level:      1000
Time taken for tests:   5.001 seconds
Complete requests:      46560
Failed requests:        0
Keep-Alive requests:    46560
Total transferred:      8287680 bytes
HTML transferred:       1117440 bytes
Requests per second:    9310.72 [#/sec] (mean)
Time per request:       107.403 [ms] (mean)
Time per request:       0.107 [ms] (mean, across all concurrent requests)
Transfer rate:          1618.47 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   8.8      0      78
Processing:     0  103  47.8     98     635
Waiting:        0  103  47.8     98     635
Total:          0  105  47.9     99     635

Percentage of the requests served within a certain time (ms)
  50%     99
  66%    114
  75%    124
  80%    130
  90%    160
  95%    184
  98%    235
  99%    270
 100%    635 (longest request)

==============================================
Vert.x, concurrency 1000
http://localhost:8090/micro/api/ping
==============================================
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 5000 requests
Completed 10000 requests
Completed 15000 requests
Completed 20000 requests
Completed 25000 requests
Completed 30000 requests
Completed 35000 requests
Completed 40000 requests
Completed 45000 requests
Completed 50000 requests
Finished 50000 requests


Server Software:        
Server Hostname:        localhost
Server Port:            8090

Document Path:          /micro/api/ping
Document Length:        24 bytes

Concurrency Level:      1000
Time taken for tests:   0.909 seconds
Complete requests:      50000
Failed requests:        0
Keep-Alive requests:    50000
Total transferred:      5950000 bytes
HTML transferred:       1200000 bytes
Requests per second:    54995.94 [#/sec] (mean)
Time per request:       18.183 [ms] (mean)
Time per request:       0.018 [ms] (mean, across all concurrent requests)
Transfer rate:          6391.13 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   3.1      0      77
Processing:     5   11   3.2     10      27
Waiting:        5   11   3.2     10      27
Total:          5   12   5.2     10      99

Percentage of the requests served within a certain time (ms)
  50%     10
  66%     11
  75%     11
  80%     12
  90%     16
  95%     17
  98%     23
  99%     42
 100%     99 (longest request)
```