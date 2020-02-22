package com.github.chrisgleissner.microservice.wildfly.rest;

import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;



public class PingResourceIT {

    @Test
    public void ping() {
        get("/wildfly-microservice/api/ping").then()
                .statusCode(200)
                .assertThat().body("pong", is(not(blankString())));
    }
}