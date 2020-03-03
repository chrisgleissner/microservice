package com.github.chrisgleissner.microservice.quarkus.securedping;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class SecuredPingResourceTest {

    @Test
    void ping() {
        get("/api/securedPing").then().statusCode(401);
    }
}