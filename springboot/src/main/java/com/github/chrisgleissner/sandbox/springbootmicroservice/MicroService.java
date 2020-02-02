package com.github.chrisgleissner.sandbox.springbootmicroservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @Slf4j
public class MicroService {

	public static void main(String[] args) {
		SpringApplication.run(MicroService.class, args);
	}
}
