package com.github.chrisgleissner.microservice.springboot.fixture;

import com.github.chrisgleissner.microservice.springboot.security.auth.AuthService;
import com.github.chrisgleissner.microservice.springboot.security.WebSecurityConfig;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackageClasses = {AuthService.class, WebSecurityConfig.class})
public @interface SecurityAwareTest {
}
