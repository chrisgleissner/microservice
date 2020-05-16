package com.github.chrisgleissner.microservice.openfeign;

import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class FeignFactory {

    public static <T> T createClient(Class<T> clientClass, String url) {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(clientClass))
                .logLevel(Logger.Level.FULL)
                .target(clientClass, url);
    }
}
