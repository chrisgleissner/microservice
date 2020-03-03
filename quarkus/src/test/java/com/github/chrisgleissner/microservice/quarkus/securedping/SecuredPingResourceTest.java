package com.github.chrisgleissner.microservice.quarkus.securedping;

import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.github.chrisgleissner.microservice.quarkus.jwt.JwtTokenGenerator.createAuthorizationHeader;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest @Slf4j
public class SecuredPingResourceTest {

    @Test
    public void ping() {
        String body = given().log().all().header("Authorization", createAuthorizationHeader())
                .when().get("/api/securedPing")
                .then().statusCode(200).extract().asString();
        log.info("Response body: {}", body);
        assertThat(body).isEqualTo("hello + jdoe@quarkus.io, isSecure: false, authScheme: Bearer, hasJWT: true");
    }

    @Test
    public void pingNotAuthorized() {
        given().log().all()
                .when().get("/api/securedPing")
                .then().statusCode(401);
    }
}