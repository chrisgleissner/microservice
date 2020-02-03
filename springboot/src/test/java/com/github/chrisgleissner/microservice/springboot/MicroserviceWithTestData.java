package com.github.chrisgleissner.microservice.springboot;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import org.junit.jupiter.api.Test;

@RestIT
public class MicroserviceWithTestData {

    @Test
    public void startWithTestDataAndWait() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}
