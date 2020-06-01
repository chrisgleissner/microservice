package com.github.chrisgleissner.microservice.openfeign.java7;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.netflix.config.ConfigurationManager;
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
import org.apache.commons.configuration.MapConfiguration;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FeignClientFactory {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeignClientFactory.class);

    private FeignClientFactory() {
        throw new UnsupportedOperationException();
    }

    public static synchronized Builder builder(Map<String, Object> properties) {
        if (properties != null) {
            if (ConfigurationManager.isConfigurationInstalled()) {
                log.warn("Not installing custom config since already installed");
            } else {
                log.info("Installing custom config: {}", properties);
                ConfigurationManager.install(new MapConfiguration(properties));
            }
        }
        return new FeignClientFactory.Builder();
    }

    public static synchronized Builder builder() {
        return builder(null);
    }

    public static class Builder {
        private static final List<Module> JACKSON_MODULES = Collections.<Module>singletonList(new JodaModule());

        private Encoder encoder = new JacksonEncoder(JACKSON_MODULES);
        private Decoder decoder = new JacksonDecoder(JACKSON_MODULES);
        private Contract contract;

        private Builder() {
        }

        public Builder encoder(Encoder encoder) {
            this.encoder = encoder;
            return this;
        }

        public Builder decoder(Decoder decoder) {
            this.decoder = decoder;
            return this;
        }

        public Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public <T> T build(Class<T> clientClass, String url) {
            long startTime = System.nanoTime();
            final Feign.Builder builder = Feign.builder();
            if (contract != null)
                builder.contract(contract);
            T client = builder.client(createRibbonClient())
                    .encoder(encoder).decoder(decoder)
                    .logger(new Slf4jLogger(clientClass)).logLevel(Logger.Level.FULL)
                    .target(clientClass, url);
            log.info("Created Feign client for {} and URL {}: {} [{}ms]", clientClass, url, client, (System.nanoTime() - startTime) / 1000000);
            return client;
        }

        private RibbonClient createRibbonClient() {
            return RibbonClient.builder()
                    .lbClientFactory(new LBClientFactory.Default())
                    .delegate(new ApacheHttpClient()).build();
        }
    }
}
