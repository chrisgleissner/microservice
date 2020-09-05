package com.github.chrisgleissner.microservice.springboot.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component @Slf4j
public class ServerVerticle extends AbstractVerticle {
    private @Autowired PingVerticle pingVerticle;
    private @Value("${server.port.vertx}") int port;
    private @Value("${server.servlet.context-path}") String contextPath;

    @Override
    public void start() throws Exception {
        long startTime = System.nanoTime();
        super.start();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get(contextPath + "/api/ping").handler(pingVerticle::handlePing);
        router.get(contextPath + "/api/ping/blocking").handler(pingVerticle::handlePingBlocking);
        httpServer.requestHandler(router).listen(port);
        log.info("Vert.x started on port {} in {}ms and listening at {}", port,
                (System.nanoTime() - startTime) / 1000_000, contextPath);
    }
}