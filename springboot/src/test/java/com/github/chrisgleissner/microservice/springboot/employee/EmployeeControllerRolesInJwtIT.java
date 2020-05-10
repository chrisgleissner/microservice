package com.github.chrisgleissner.microservice.springboot.employee;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtFixture.adminJwt;
import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtFixture.userJwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(SpringExtension.class) @RestIT
@TestPropertySource(properties = "security.jwt.encodeRolesInJwt=true")
class EmployeeControllerRolesInJwtIT extends AbstractEmployeeControllerIT {
}
