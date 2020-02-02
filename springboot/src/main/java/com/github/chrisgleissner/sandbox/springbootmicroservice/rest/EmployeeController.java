package com.github.chrisgleissner.sandbox.springbootmicroservice.rest;

import com.github.chrisgleissner.sandbox.springbootmicroservice.entity.Employee;
import com.github.chrisgleissner.sandbox.springbootmicroservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController @RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EmployeeController {
	private final EmployeeRepository userRepository;

	@GetMapping("/{id}")
	public Employee findById(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find employee by ID " + id));
	}

	@GetMapping
	public Iterable<Employee> findAll(@RequestParam(required=false) String lastName) {
		return Optional.ofNullable(lastName).map(userRepository::findByLastname).orElseGet(userRepository::findAll);
	}
}