package com.github.chrisgleissner.microservice.springboot.fixture;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonFixture {

    static String json(ObjectMapper objectMapper, Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("Could not write as JSON: " + o, e);
        }
    }

    static <T> T fromJson(ObjectMapper objectMapper, String json, Class<T> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not create %s from JSON: %s", targetClass, json, e));
        }
    }
}
