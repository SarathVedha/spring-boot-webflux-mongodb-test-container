package com.vedha.webflux.service;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.utill.EmployeeSortField;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EmployeeService {

    Mono<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO);

    Mono<EmployeeDTO> getEmployeeById(String employeeId);

    Flux<EmployeeDTO> getAllEmployees();

    Mono<EmployeeDTO> updateEmployee(String employeeId, EmployeeDTO employeeDTO);

    Mono<Map<String, String>> deleteEmployee(String employeeId);

    Mono<Map<String, String>> totalEmployees();

    Mono<EmployeeDTO> getEmployeeByName(String name);

    Flux<EmployeeDTO> getAllEmployeesBySort(Sort.Direction sortDirection, EmployeeSortField sortField);

    Mono<PageImpl<EmployeeDTO>> getAllEmployeesByPage(int pageNumber, int sizePerPage, Sort.Direction sortDirection, EmployeeSortField sortField);
}
