package com.github.chrisgleissner.microservice.wildfly.rest;

import javax.ejb.Singleton;

@Singleton
class PingService {

    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
