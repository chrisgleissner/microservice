package com.github.chrisgleissner.microservice.springboot.ping;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) @RestIT
class PingControllerIT {
    @Autowired TestRestTemplate template;

    @Test
    void ping() {
        ResponseEntity<PingResponse> response = template.getForEntity("/api/ping", PingResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().pong).isGreaterThan(0);
    }

    @Value @NoArgsConstructor(force = true) @AllArgsConstructor
    public static class PingResponse {
        long pong;
    }
}
