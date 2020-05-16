package com.github.chrisgleissner.microservice.openfeign.security;

import com.github.chrisgleissner.microservice.openfeign.FeignFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

// TODO Start up service and rename to IT
public class AuthClientMT {

    @Test
    public void getJwt() {
        AuthClient client = FeignFactory.createClient(AuthClient.class, "http://localhost:8080/micro");
        String jwt = client.getJwt(ImmutableUserCredentials.builder().username("user").password("secret").build());
        assertThat(jwt).isNotEmpty();
    }
}