package com.github.chrisgleissner.microservice.wildfly.ping;

import javax.ejb.Singleton;

@Singleton
class PingService {

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
