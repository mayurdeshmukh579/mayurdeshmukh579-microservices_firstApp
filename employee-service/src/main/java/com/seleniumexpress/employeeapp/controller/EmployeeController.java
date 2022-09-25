package com.seleniumexpress.employeeapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumexpress.employeeapp.entity.Employee;
import com.seleniumexpress.employeeapp.response.EmployeeResponse;
import com.seleniumexpress.employeeapp.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@GetMapping("/employees/{id}")
	ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable("id") int id){
		
		
		 EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
	}
	
	@PostMapping("/employees")
	public EmployeeResponse createEmploye(@RequestBody EmployeeResponse employeeResponse) {
		
		 EmployeeResponse employeeRep = employeeService.createEmployee(employeeResponse);
		 
		
		return null;
		
	}
	
}





