package com.github.chrisgleissner.microservice.openfeign.java7.security;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignFactory;
import feign.codec.StringDecoder;

public class AuthFixture {

    public static String getJwt() {
        return FeignFactory.createClient(AuthClient.class, FeignFactory.DEFAULT_URL, FeignFactory.JACKSON_ENCODER, new StringDecoder(), null)
                .getJwt(ImmutableUserCredentials.builder().username("user").password("secret").build());
    }
}
