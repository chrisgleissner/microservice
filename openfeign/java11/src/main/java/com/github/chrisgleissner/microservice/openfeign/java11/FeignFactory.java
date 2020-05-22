package com.github.chrisgleissner.microservice.openfeign.java11;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class FeignFactory {
    public static final JacksonEncoder JACKSON_ENCODER = new JacksonEncoder();
    public static final JacksonDecoder JACKSON_DECODER = new JacksonDecoder();

    public static <T> T createClient(Class<T> clientClass, String url) {
        return createClient(clientClass, url, JACKSON_ENCODER, JACKSON_DECODER, null);
    }

    public static <T> T createClient(Class<T> clientClass, String url, Encoder encoder, Decoder decoder, Contract contract) {
        Feign.Builder builder = Feign.builder();
        if (contract != null)
            builder.contract(contract);
        return builder.client(new ApacheHttpClient())
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Slf4jLogger(clientClass))
                .logLevel(Logger.Level.FULL)
                .target(clientClass, url);
    }
}
