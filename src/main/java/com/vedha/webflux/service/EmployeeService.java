package com.vedha.webflux.service;

import com.vedha.webflux.dto.EmployeeDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EmployeeService {

    Mono<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO);

    Mono<EmployeeDTO> getEmployeeById(String employeeId);

    Flux<EmployeeDTO> getAllEmployees();

    Mono<EmployeeDTO> updateEmployee(String employeeId, EmployeeDTO employeeDTO);

    Mono<Map<String, String>> deleteEmployee(String employeeId);
}
