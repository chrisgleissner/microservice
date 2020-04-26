package com.github.chrisgleissner.microservice.springboot.ping;

import com.github.chrisgleissner.microservice.springboot.rest.security.WebSecurityConfig;
import com.github.chrisgleissner.microservice.springboot.rest.security.user.AppUserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PingController.class)
@ComponentScan(basePackageClasses = WebSecurityConfig.class)
class PingControllerTest {
    @Autowired MockMvc mvc;
    @MockBean PingService pingService;
    @MockBean AppUserRepo appUserRepo;

    @Test
    void ping() throws Exception {
        when(pingService.currentTimeMillis()).thenReturn(1L);
        mvc.perform(get("/api/ping").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}
