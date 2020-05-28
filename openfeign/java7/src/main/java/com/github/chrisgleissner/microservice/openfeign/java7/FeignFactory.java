package com.github.chrisgleissner.microservice.openfeign.java7;

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
import feign.ribbon.LBClientFactory;
import feign.ribbon.RibbonClient;
import feign.slf4j.Slf4jLogger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeignFactory {
    private static final List<Module> JACKSON_MODULES = Collections.<Module>singletonList(new JodaModule());
    public static final JacksonEncoder JACKSON_ENCODER = new JacksonEncoder(JACKSON_MODULES);
    public static final JacksonDecoder JACKSON_DECODER = new JacksonDecoder(JACKSON_MODULES);

    // Ribbon properties are read via Archaicus from classpath:config.properties, see https://github.com/Netflix/archaius/wiki/Getting-Started
    // This may be overridden via -Darchaius.configurationSource.additionalUrls=file:///apps/myapp/application.properties
    // 'microservice' refers to a property prefix
    public static final String DEFAULT_URL = "http://microservice";

    public static <T> T createClient(Class<T> clientClass) {
        return createClient(clientClass, DEFAULT_URL);
    }

    public static <T> T createClient(Class<T> clientClass, String url) {
        return createClient(clientClass, url, JACKSON_ENCODER, JACKSON_DECODER, null);
    }

    public static <T> T createClient(Class<T> clientClass, String url, Encoder encoder, Decoder decoder, Contract contract) {
        final Feign.Builder builder = Feign.builder();
        if (contract != null)
            builder.contract(contract);
        return builder.client(createRibbonClient()).encoder(encoder).decoder(decoder)
                .logger(new Slf4jLogger(clientClass)).logLevel(Logger.Level.FULL)
                .target(clientClass, url);
    }

    private static RibbonClient createRibbonClient() {
        return RibbonClient.builder().lbClientFactory(new LBClientFactory.Default()).delegate(new ApacheHttpClient()).build();
    }
}
