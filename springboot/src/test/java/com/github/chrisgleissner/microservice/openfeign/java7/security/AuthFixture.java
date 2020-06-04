package com.github.chrisgleissner.microservice.openfeign.java7.security;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClient;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
import feign.codec.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthFixture {
    public static final String USER = "user";
    public static final String SECRET = "secret";
    private static Logger log = LoggerFactory.getLogger(AuthFixture.class);

    public static String getJwt() {
        try {
            AuthClient authClient = FeignClient.builder().decoder(new StringDecoder()).build(AuthClient.class, FeignClientUrls.MICROSERVICE_URL);
            return authClient.getJwt(ImmutableUserCredentials.builder().username(USER).password(SECRET).build());
        } catch (Exception e) {
            String msg = "Could not get JWT";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
