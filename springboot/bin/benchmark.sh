#!/bin/bash

JOB_DURATION=5

function stage {
  STAGE=$1
  CONCURRENCY=$2
  printf "\n##############################################\n"
  printf "# $STAGE, concurrency $CONCURRENCY\n"
  printf "##############################################\n"
  job "Spring Web" $CONCURRENCY http://localhost:8080/micro/api/ping
  job "Spring Webflux" $CONCURRENCY http://localhost:8080/micro/api/ping/flux
  job "Vert.x" $CONCURRENCY http://localhost:8090/micro/api/ping
}

function job {
  FRAMEWORK=$1
  CONCURRENCY=$2
  URL=$3
  printf "\n==============================================\n"
  printf "$FRAMEWORK, concurrency $CONCURRENCY\n"
  printf "$URL\n"
  printf "==============================================\n"
  ab -kc $CONCURRENCY -t $JOB_DURATION $URL
}

stage Warm-up 10
stage Benchmark 1
stage Benchmark 100
stage Benchmark 1000
