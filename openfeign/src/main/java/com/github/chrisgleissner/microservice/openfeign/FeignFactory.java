package com.github.chrisgleissner.microservice.openfeign;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;

import java.util.Arrays;
import java.util.List;

public class FeignFactory {
    private static final List<Module> JACKSON_MODULES = Arrays.<Module>asList(new JodaModule());
    public static final JacksonEncoder JACKSON_ENCODER = new JacksonEncoder(JACKSON_MODULES);
    public static final JacksonDecoder JACKSON_DECODER = new JacksonDecoder(JACKSON_MODULES);

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
