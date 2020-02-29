package com.github.chrisgleissner.microservice.quarkus.ping;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "ping")
public interface PingConfig {
    String getResponseMessage();
    int getResponseDelayInMillis();
}
