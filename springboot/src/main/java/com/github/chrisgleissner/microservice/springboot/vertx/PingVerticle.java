package com.github.chrisgleissner.microservice.springboot.vertx;

import com.github.chrisgleissner.microservice.springboot.ping.PingService;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.apache.http.HttpStatus.SC_OK;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component @RequiredArgsConstructor
public class PingVerticle extends AbstractVerticle {
    private final PingService pingService;

    void handlePingBlocking(RoutingContext ctx) {
        vertx.executeBlocking(promise -> sendPingResponse(ctx), true, asyncResult -> {});
    }

    void handlePing(RoutingContext ctx) {
        sendPingResponse(ctx);
    }

    private void sendPingResponse(RoutingContext ctx) {
        ctx.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE).setStatusCode(SC_OK)
                .end("{\"pong\":\"" + pingService.currentTimeMillis() + "\"}");
    }
}
