package com.github.chrisgleissner.microservice.springboot.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.microservice.springboot.employee.EmployeeController;
import com.github.chrisgleissner.microservice.springboot.employee.EmployeeRepository;
import com.github.chrisgleissner.microservice.springboot.fixture.SecurityAwareTest;
import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUser;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUserRepo;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.fromJson;
import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.json;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
@SecurityAwareTest
class AuthControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean EmployeeRepository userRepository;
    @MockBean AppUserRepo appUserRepo;

    @Test
    void getJwtAndAuthorization() throws Exception {
        val username = "john";
        val password = "pwd";
        val roles = List.of("role1", "role2");
        when(appUserRepo.findByUsername(eq(username))).thenReturn(Optional.of(new AppUser(username, bCryptPasswordEncoder.encode(password), roles)));

        String jwt = mvc.perform(post("/api/auth/jwts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(objectMapper, new UserCredentials(username, password)))
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String authorizationInfoJson = mvc.perform(post("/api/auth/authorizations")
                .contentType(MediaType.TEXT_PLAIN)
                .content(jwt)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AuthorizationInfo authorizationInfo = fromJson(objectMapper, authorizationInfoJson, AuthorizationInfo.class);
        assertThat(authorizationInfo.getUsername()).isEqualTo(username);
        assertThat(authorizationInfo.getRoles()).containsExactlyElementsOf(roles);
    }
}
