package com.github.chrisgleissner.microservice.quarkus.ping;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class PingResourceTest {
    @Inject PingConfig pingConfig;
    @TestHTTPResource("/api/ping") URL url;

    @Test
    void ping() {
        get("/api/ping").then().statusCode(200).assertThat().body("pong", is(not(blankString())));
    }

    @Test
    void pingViaTestHttpResource() throws IOException {
        try (InputStream in = url.openStream()) {
            Scanner s = new Scanner(in).useDelimiter("\\A");
            assertThat(s.hasNext() ? s.next() : "").contains("pong");
        }
    }

    @Test
    public void configExists() {
        assertThat(pingConfig.getResponseMessage()).isEqualTo("pong");
        assertThat(ConfigProvider.getConfig().getValue("ping.response-message", String.class)).isEqualTo("pong");
    }

    @Test
    public void configSupportsIntegers() {
        assertThat(pingConfig.getResponseDelayInMillis()).isEqualTo(5);
    }
}