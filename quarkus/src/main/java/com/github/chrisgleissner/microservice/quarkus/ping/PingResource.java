package com.github.chrisgleissner.microservice.quarkus.ping;

import lombok.RequiredArgsConstructor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping") @RequiredArgsConstructor
public class PingResource {
    private final PingService pingService;
    private final PingConfig pingConfig;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() throws InterruptedException {
        Thread.sleep(pingConfig.getResponseDelayInMillis());
        return String.format("{\"%s\":\"%s\"}", pingConfig.getResponseMessage(), pingService.currentTimeMillis());
    }
}