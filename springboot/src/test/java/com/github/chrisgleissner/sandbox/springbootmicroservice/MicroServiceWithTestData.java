package com.github.chrisgleissner.sandbox.springbootmicroservice;

import com.github.chrisgleissner.sandbox.springbootmicroservice.rest.RestIT;
import org.junit.jupiter.api.Test;

@RestIT
public class MicroServiceWithTestData {

    @Test
    public void startWithTestDataAndWait() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}
