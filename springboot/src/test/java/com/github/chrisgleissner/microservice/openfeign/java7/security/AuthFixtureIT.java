package com.github.chrisgleissner.microservice.openfeign.java7.security;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientFactory;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
import feign.codec.StringDecoder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture.SECRET;
import static com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;

class AuthFixtureIT {
    private static final ImmutableUserCredentials USER_CREDENTIALS = ImmutableUserCredentials.builder().username(USER).password(SECRET).build();

    @Test
    void canGetJwt() {
        // Property override at client construction time
        Map<String, Object> properties = new HashMap<>();
        properties.put("microservice.ribbon.listOfServers", "localhost:8080,localhost:8079");

        AuthClient authClient = FeignClientFactory.builder(properties)
                .decoder(new StringDecoder())
                .build(AuthClient.class, FeignClientUrls.MICROSERVICE_URL);
        String jwt = authClient.getJwt(USER_CREDENTIALS);
        assertThat(jwt).isNotEmpty();

    }
}