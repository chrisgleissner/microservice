package com.github.chrisgleissner.microservice.springboot.employee;

import com.github.chrisgleissner.microservice.springboot.security.annotation.IsAdmin;
import com.github.chrisgleissner.microservice.springboot.security.annotation.IsUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController @RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor @Slf4j
public class EmployeeController {
	private final EmployeeRepository employeeRepository;

	@IsUser
	@GetMapping("/{id}")
	public Employee findById(@PathVariable Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find employee by ID " + id));
	}

	@IsUser
	@GetMapping
	public Iterable<Employee> findAll(@RequestParam(required=false) String lastName) {
		return Optional.ofNullable(lastName).map(employeeRepository::findByLastname).orElseGet(employeeRepository::findAll);
	}

	@IsAdmin
	@PostMapping
	public Employee create(@RequestBody Employee employee) {
		Employee savedEmployee = employeeRepository.save(employee);
		log.info("Created {}", savedEmployee);
		return savedEmployee;
	}
}