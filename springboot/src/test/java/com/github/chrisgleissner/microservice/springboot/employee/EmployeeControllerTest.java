package com.github.chrisgleissner.microservice.springboot.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.microservice.springboot.employee.Employee;
import com.github.chrisgleissner.microservice.springboot.employee.EmployeeController;
import com.github.chrisgleissner.microservice.springboot.employee.EmployeeRepository;
import com.github.chrisgleissner.microservice.springboot.rest.security.WebSecurityConfig;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create.UserConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
@ComponentScan(basePackageClasses = WebSecurityConfig.class)
class EmployeeControllerTest {
    private static final String PATH = "/api/employee";
    private static final Employee JOHN_DOE = new Employee(1, "John", "Doe", LocalDate.of(1960, 1, 1));
    private static final Employee JILL_ZOE = new Employee(2, "Jill", "Zoe", LocalDate.of(1961, 1, 1));
    private static final List<Employee> EMPLOYEES = List.of(JOHN_DOE, JILL_ZOE);

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EmployeeRepository userRepository;

    @Test
    @WithMockUser(username = UserConstants.USER_NAME, roles = {UserConstants.USER_ROLE})
    void findAll() throws Exception {
        when(userRepository.findAll()).thenReturn(EMPLOYEES);
        assertThat(findUsers(PATH)).isEqualTo(EMPLOYEES);
    }


    @Test
    void findAllUnauthorized() throws Exception {
        ResultActions resultActions = mvc.perform(get(PATH));
        resultActions.andExpect(mvcResult -> assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser(username = UserConstants.USER_NAME, roles = {UserConstants.USER_ROLE})
    void findById() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.of(JOHN_DOE));
        assertThat(findUser(PATH + "/" + JOHN_DOE.getId())).isEqualTo(JOHN_DOE);
    }

    @Test
    @WithMockUser(username = UserConstants.USER_NAME, roles = {UserConstants.USER_ROLE})
    void findByIdNotFound() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        mvc.perform(get(PATH + "/" + 3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = UserConstants.USER_NAME, roles = {UserConstants.USER_ROLE})
    void findByLastname() throws Exception {
        when(userRepository.findByLastname(any())).thenReturn(List.of(JOHN_DOE));
        assertThat(findUsers(PATH + "?lastName=Doe")).containsExactly(JOHN_DOE);
    }

    private List<Employee> findUsers(String path) throws Exception {
        return List.of(objectMapper.readValue(find(path), Employee[].class));
    }

    private Employee findUser(String path) throws Exception {
        return objectMapper.readValue(find(path), Employee.class);
    }

    private String find(String path) throws Exception {
        return mvc.perform(get(path).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}
