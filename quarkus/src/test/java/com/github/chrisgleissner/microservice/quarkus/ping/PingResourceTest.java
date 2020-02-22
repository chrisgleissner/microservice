package com.github.chrisgleissner.microservice.quarkus.ping;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class PingResourceTest {

    @Test
    void ping() {
        get("/api/ping").then()
                .statusCode(200)
                .assertThat().body("pong", is(not(blankString())));
    }
}