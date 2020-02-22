package com.github.chrisgleissner.microservice.ping;

import javax.inject.Singleton;

@Singleton
class PingService {

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
