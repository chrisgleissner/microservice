package com.github.chrisgleissner.microservice.quarkus.ping;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.ApplicationScoped;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class PingResourceServiceMockTest {

    @Test
    void ping() {
        assertThat(get("/api/ping").then().statusCode(200).extract().asString()).isEqualTo("{\"pong\":\"1\"}");
    }

    @Mock @ApplicationScoped
    public static class MockPingService extends PingService {
        long currentTimeMillis() {
            return 1;
        }
    }
}
