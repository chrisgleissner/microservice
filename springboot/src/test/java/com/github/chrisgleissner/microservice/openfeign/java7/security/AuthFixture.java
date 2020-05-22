package com.github.chrisgleissner.microservice.openfeign.java7.security;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignFactory;
import feign.codec.StringDecoder;

public class AuthFixture {

    public static String getJwt(String serviceUrl) {
        AuthClient client = FeignFactory.createClient(AuthClient.class, serviceUrl, FeignFactory.JACKSON_ENCODER,
                new StringDecoder(), null);
        return client.getJwt(ImmutableUserCredentials.builder().username("user").password("secret").build());
    }
}
