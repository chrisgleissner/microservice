package com.github.chrisgleissner.microservice.springboot.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.microservice.springboot.rest.security.WebSecurityConfig;
import com.github.chrisgleissner.microservice.springboot.rest.security.user.AppUserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.fromJson;
import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.json;
import static com.github.chrisgleissner.microservice.springboot.rest.security.user.UserConstants.ADMIN_ROLE;
import static com.github.chrisgleissner.microservice.springboot.rest.security.user.UserConstants.ADMIN_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
@ComponentScan(basePackageClasses = WebSecurityConfig.class)
class EmployeeControllerTest {
    private static final String PATH = "/api/employee";
    private static final Employee JOHN_DOE = new Employee(1L, "John", "Doe", LocalDate.of(1960, 1, 1));
    private static final Employee JILL_ZOE = new Employee(2L, "Jill", "Zoe", LocalDate.of(1961, 1, 1));
    private static final List<Employee> EMPLOYEES = List.of(JOHN_DOE, JILL_ZOE);

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EmployeeRepository userRepository;
    @MockBean AppUserRepo appUserRepo;

    @Test
    @WithMockUser
    void findAll() throws Exception {
        when(userRepository.findAll()).thenReturn(EMPLOYEES);
        assertThat(findUsers(PATH)).isEqualTo(EMPLOYEES);
    }

    @Test
    @WithMockUser
    void findById() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.of(JOHN_DOE));
        assertThat(findUser(PATH + "/" + JOHN_DOE.getId())).isEqualTo(JOHN_DOE);
    }

    @Test
    @WithMockUser
    void findByIdNotFound() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        mvc.perform(get(PATH + "/" + 3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void findByLastname() throws Exception {
        when(userRepository.findByLastname(any())).thenReturn(List.of(JOHN_DOE));
        assertThat(findUsers(PATH + "?lastName=Doe")).containsExactly(JOHN_DOE);
    }

    @Test
    void getUnauthorizedIfNoUserSpecified() throws Exception {
        mvc.perform(get(PATH)).andExpect(status().isUnauthorized());
    }

    @Test
    void postUnauthorized() throws Exception {
        mvc.perform(post(PATH)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void postForbidden() throws Exception {
        mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json(objectMapper, new Employee("Foo", "Bar", LocalDate.of(2000, 1, 1)))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_USER_NAME, roles = {ADMIN_ROLE})
    void postWorks() throws Exception {
        when(userRepository.save(Mockito.any())).then(returnsFirstArg());
        Employee employee = new Employee("Foo", "Bar", LocalDate.of(2000, 1, 1));
        String responseString = mvc.perform(post(PATH)
                .contentType(APPLICATION_JSON_VALUE)
                .content(json(objectMapper, employee)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Employee savedEmployee = fromJson(objectMapper, responseString, Employee.class);
        assertThat(savedEmployee).isEqualToIgnoringGivenFields(employee, "id");
    }

    private List<Employee> findUsers(String path) throws Exception {
        return List.of(fromJson(objectMapper, find(path), Employee[].class));
    }

    private Employee findUser(String path) throws Exception {
        return fromJson(objectMapper, find(path), Employee.class);
    }

    private String find(String path) throws Exception {
        return mvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}
