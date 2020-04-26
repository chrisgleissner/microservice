package com.github.chrisgleissner.microservice.springboot.ping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// Unsecured
@RestController @RequestMapping(value = "/api/ping") @RequiredArgsConstructor
public class PingController {
    private final PingService pingService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public String ping() {
        return "{\"pong\":\"" + pingService.currentTimeMillis() + "\"}";
    }
}
