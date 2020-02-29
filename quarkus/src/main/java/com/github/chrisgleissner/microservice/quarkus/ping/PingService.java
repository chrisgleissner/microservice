package com.github.chrisgleissner.microservice.quarkus.ping;

import javax.inject.Singleton;

@Singleton
class PingService {
    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
