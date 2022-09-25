package com.seleniumexpress.employeeapp.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.seleniumexpress.employeeapp.entity.Employee;
import com.seleniumexpress.employeeapp.repo.EmployeeRepo;
import com.seleniumexpress.employeeapp.response.AddressResponse;
import com.seleniumexpress.employeeapp.response.EmployeeResponse;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepo EmployeeRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private WebClient webClient;

	 @Autowired
	 private RestTemplate restTemplate; // null
	 
	 @Autowired
	 private DiscoveryClient discoveryClient;

	// @Value("${adressservice.base.url}")
	// private String addressBaseURL;

//	public EmployeeService(@Value("${adressservice.base.url}") String addressBaseURL,
//			RestTemplateBuilder builder) {
//		
//		this.restTemplate= builder
//				           .rootUri(addressBaseURL)
//				           .build();
//	}

	public EmployeeResponse getEmployeeById(int id) {

		// employee -> EmployeeResponse
		Employee employee = EmployeeRepo.findById(id).get(); // db call -> 10

		EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

		// 10 sec
		AddressResponse addressResponse = callToAddressUsingWebClient(id);

		employeeResponse.setAddressResponse(addressResponse);

		return employeeResponse;

	}

	private AddressResponse callToAddressUsingWebClient(int id) {
		List<ServiceInstance> instances = discoveryClient.getInstances("address-service");
		ServiceInstance serviceInstance = instances.get(0);
		String uri = serviceInstance.getUri().toString();
		
		return restTemplate.getForObject(uri+"/address-app/api/address/{id}", AddressResponse.class, id);
	}

	private AddressResponse callingAddressServiceUsingRESTTemplate(int id) {
		return restTemplate.getForObject("http://localhost:8081/address-app/api/address/{id}", AddressResponse.class , id);
	}

	public EmployeeResponse createEmployee(EmployeeResponse employeeResponse) {

		EmployeeResponse employeeResponse1 = null ;
		
		Employee employee= modelMapper.map(employeeResponse, Employee.class);

		// 10 sec
		AddressResponse addressResponse = createToAddressUsingWebClient(employeeResponse.getAddressResponse());

		employeeResponse1 = modelMapper.map(employee, EmployeeResponse.class);
		
		employeeResponse1.setAddressResponse(addressResponse);
		
		
		
		
		return employeeResponse1;
	}

	private AddressResponse createToAddressUsingWebClient(AddressResponse addressResponse) {


		return restTemplate.postForObject("http://localhost:8081/address-app/api/address", addressResponse, AddressResponse.class);
	
	}

}
