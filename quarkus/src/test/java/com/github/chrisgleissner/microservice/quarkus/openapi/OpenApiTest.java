package com.github.chrisgleissner.microservice.quarkus.openapi;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class OpenApiTest {

    @Test
    void openApiJson() {
        String body = given().header("Accept", "application/json")
        .when().get("/openapi").then().statusCode(200).extract().asString();
        assertThat(body).contains("\"paths\" : {\n" +
                "    \"/api/employee\" : {\n" +
                "      \"get\" : {\n" +
                "        \"parameters\" : [ {\n" +
                "          \"name\" : \"lastName\",\n" +
                "          \"in\" : \"query\",\n" +
                "          \"schema\" : {\n" +
                "            \"type\" : \"string\"");
    }

    @Test
    void openApiYml() {
        String body = get("/openapi").then().statusCode(200).extract().asString();
        assertThat(body).contains("paths:\n" +
                "  /api/employee:\n" +
                "    get:\n" +
                "      parameters:\n" +
                "      - name: lastName\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          type: string\n");
    }

    @Test
    void swaggerUi() {
        String body = get("/swagger-ui").then().statusCode(200).extract().asString();
        assertThat(body).contains("Swagger UI");
    }
}
