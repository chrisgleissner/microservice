package com.github.chrisgleissner.microservice.springboot.ping;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping(value = "/api/ping")
public class PingController {

    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String ping() {
        return "{\"pong\":\"" + pingService.currentTimeMillis() + "\"}";
    }
}
