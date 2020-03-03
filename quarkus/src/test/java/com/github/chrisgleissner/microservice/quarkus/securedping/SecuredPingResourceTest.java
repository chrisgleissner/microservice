package com.github.chrisgleissner.microservice.quarkus.securedping;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static com.github.chrisgleissner.microservice.quarkus.jwt.JwtTokenGenerator.createAuthorizationHeader;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

@QuarkusTest
class SecuredPingResourceTest {

    @Test
    void ping() {
        given().header("Authorization", createAuthorizationHeader())
                .when().get("/api/securedPing")
                .then().statusCode(200);
    }

    @Test
    void pingNotAuthorized() {
        get("/api/securedPing").then().statusCode(401);
    }
}