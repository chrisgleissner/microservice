package com.github.chrisgleissner.sandbox.springbootmicroservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.sandbox.springbootmicroservice.entity.Employee;
import com.github.chrisgleissner.sandbox.springbootmicroservice.repository.EmployeeRepository;
import com.github.chrisgleissner.sandbox.springbootmicroservice.rest.EmployeeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    private static final String PATH = "/api/employee";
    private static final Employee JOHN_DOE = new Employee(1, "John", "Doe", LocalDate.of(1960, 1, 1));
    private static final Employee JILL_ZOE = new Employee(2, "Jill", "Zoe", LocalDate.of(1961, 1, 1));
    private static final List<Employee> EMPLOYEES = List.of(JOHN_DOE, JILL_ZOE);

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EmployeeRepository userRepository;

    @Test
    void findAll() throws Exception {
        when(userRepository.findAll()).thenReturn(EMPLOYEES);
        assertThat(findUsers(PATH)).isEqualTo(EMPLOYEES);
    }

    @Test
    void findById() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.of(JOHN_DOE));
        assertThat(findUser(PATH + "/" + JOHN_DOE.getId())).isEqualTo(JOHN_DOE);
    }

    @Test
    void findByIdNotFound() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        mvc.perform(get(PATH + "/" + 3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
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
