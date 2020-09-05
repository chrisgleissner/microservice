package com.github.chrisgleissner.microservice.springboot.vertx;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class VertxConfig {
    @Autowired ServerVerticle serverVerticle;

    @PostConstruct
    public void deployVerticles() {
        Vertx.vertx().deployVerticle(serverVerticle);
    }
}
