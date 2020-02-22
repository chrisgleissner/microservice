package com.github.chrisgleissner.microservice.rest;

import javax.inject.Singleton;

@Singleton
class PingService {

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
