package com.github.chrisgleissner.microservice.springboot.employee;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class) @RestIT
@TestPropertySource(properties = "security.jwt.encodeRolesInJwt=false")
class EmployeeControllerRolesNotInJwtIT extends AbstractEmployeeControllerIT {
}
