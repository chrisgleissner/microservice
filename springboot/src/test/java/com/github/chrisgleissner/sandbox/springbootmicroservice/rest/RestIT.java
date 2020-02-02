package com.github.chrisgleissner.sandbox.springbootmicroservice.rest;

import com.github.chrisgleissner.sandbox.springbootmicroservice.MicroService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(classes = MicroService.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles
public @interface RestIT {
  @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles") String[] activeProfiles() default {"test"};
}