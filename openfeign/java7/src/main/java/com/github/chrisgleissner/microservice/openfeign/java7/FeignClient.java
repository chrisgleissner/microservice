package com.github.chrisgleissner.microservice.openfeign.java7;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.netflix.config.ConfigurationManager;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
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

public class FeignClient {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeignClient.class);

    private FeignClient() {
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
        return new FeignClient.Builder();
    }

    public static synchronized Builder builder() {
        return builder(null);
    }

    interface BuilderConsumer {
        void consume(Feign.Builder feignBuilder);
    }

    public static class Builder {
        private static final List<Module> JACKSON_MODULES = Collections.<Module>singletonList(new JodaModule());

        private Encoder encoder = new JacksonEncoder(JACKSON_MODULES);
        private Decoder decoder = new JacksonDecoder(JACKSON_MODULES);
        private List<RequestInterceptor> requestInterceptors;
        private Contract contract;
        private BuilderConsumer builderConsumer;
        private String jwt;

        private Builder() {
        }

        public Builder jwt(String jwt) {
            this.jwt = jwt;
            return this;
        }

        public Builder requestInterceptor(List<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors = requestInterceptors;
            return this;
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

        public Builder encoder(BuilderConsumer builderConsumer) {
            this.builderConsumer = builderConsumer;
            return this;
        }

        public <T> T build(Class<T> clientClass, String url) {
            long startTime = System.nanoTime();
            final Feign.Builder builder = Feign.builder()
                    .client(createRibbonClient())
                    .encoder(encoder).decoder(decoder)
                    .logger(new Slf4jLogger(clientClass))
                    .logLevel(Logger.Level.FULL);
            if (contract != null)
                builder.contract(contract);
            if (requestInterceptors != null)
                builder.requestInterceptors(requestInterceptors);
            if (builderConsumer != null)
                builderConsumer.consume(builder);
            if (jwt != null) {
                builder.requestInterceptor(new JwtRequestInterceptor(jwt));
            }
            T client = builder.target(clientClass, url);
            log.info("Created Feign client for {} and URL {}: {} [{}ms]", clientClass, url, client, (System.nanoTime() - startTime) / 1000000);
            return client;
        }

        private RibbonClient createRibbonClient() {
            return RibbonClient.builder()
                    .lbClientFactory(new LBClientFactory.Default())
                    .delegate(new ApacheHttpClient()).build();
        }

        private static class JwtRequestInterceptor implements RequestInterceptor {
            private final String jwt;

            public JwtRequestInterceptor(String jwt) {
                this.jwt = jwt;
            }

            @Override public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("Authorization", "Bearer " + jwt);
            }
        }
    }
}
