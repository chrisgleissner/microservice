package com.github.chrisgleissner.microservice.springboot.ping;

import org.springframework.stereotype.Service;

@Service
public class PingService {
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
