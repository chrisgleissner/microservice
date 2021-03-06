package com.github.chrisgleissner.microservice.springboot.ping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping(value = "/api/ping") @RequiredArgsConstructor
public class PingController {
    private final PingService pingService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public String ping() {
        return pong();
    }

    @GetMapping(path = "/flux", produces = APPLICATION_JSON_VALUE)
    private Mono<String> pingFlux() {
        return Mono.fromCallable(this::pong);
    }

    private String pong() {
        return "{\"pong\":\"" + pingService.currentTimeMillis() + "\"}";
    }
}
