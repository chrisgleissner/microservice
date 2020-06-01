package com.github.chrisgleissner.microservice.openfeign.java7;

public class FeignClientUrls {
    // Ribbon properties are read via Archaicus from classpath:config.properties, see https://github.com/Netflix/archaius/wiki/Getting-Started
    // This may be overridden via -Darchaius.configurationSource.additionalUrls=file:///apps/myapp/application.properties
    // 'microservice' refers to a property prefix
    public static final String MICROSERVICE_NAME = "microservice";
    public static final String MICROSERVICE_URL = "http://" + MICROSERVICE_NAME;
}
