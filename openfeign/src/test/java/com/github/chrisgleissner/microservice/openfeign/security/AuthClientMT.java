package com.github.chrisgleissner.microservice.openfeign.security;

import org.junit.Test;

import static com.github.chrisgleissner.microservice.openfeign.TestConstants.SERVICE_URL;
import static org.assertj.core.api.Assertions.assertThat;

// TODO Start up service and rename to IT
public class AuthClientMT {

    @Test
    public void getJwt() {
        assertThat(AuthFixture.getJwt(SERVICE_URL)).isNotEmpty();
    }
}