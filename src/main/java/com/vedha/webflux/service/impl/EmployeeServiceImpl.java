package com.vedha.webflux.service.impl;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.entity.Employee;
import com.vedha.webflux.repository.EmployeeRepository;
import com.vedha.webflux.service.EmployeeService;
import com.vedha.webflux.utill.EmployeeSortField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * EmployeeServiceImpl
 * Mono is single-value asynchronous type. It can be used to represent a single value or no value.
 * Flux is a multi-value asynchronous type. It can be used to represent zero to many values.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final ModelMapper modelMapper;

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO) {

        Employee mapped = modelMapper.map(employeeDTO, Employee.class);
        Mono<Employee> saved = employeeRepository.save(mapped);

        return saved.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public Mono<EmployeeDTO> getEmployeeById(String employeeId) {

        Mono<Employee> byId = employeeRepository.findById(employeeId);

        return byId.map(employee -> modelMapper.map(employee, EmployeeDTO.class)).switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<EmployeeDTO> getAllEmployees() {

        Flux<Employee> all = employeeRepository.findAll();

        return all.map(employee -> modelMapper.map(employee, EmployeeDTO.class)).switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDTO> updateEmployee(String employeeId, EmployeeDTO employeeDTO) {

        Mono<Employee> byId = employeeRepository.findById(employeeId);
        Mono<Employee> employeeMono = byId.flatMap(employee -> {
            employee.setName(employeeDTO.getName());
            employee.setEmail(employeeDTO.getEmail());
            employee.setAge(employeeDTO.getAge());
            employee.setDob(employeeDTO.getDob());

            return employeeRepository.save(employee);
        }).switchIfEmpty(Mono.empty());

        return employeeMono.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public Mono<Map<String, String>> deleteEmployee(String employeeId) {

        Mono<Long> longMono = employeeRepository.deleteEmployeeById(employeeId);

        return longMono.map(aLong -> Map.of("deleteCount", String.valueOf(aLong), "status", "deleted"))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Map<String, String>> totalEmployees() {

        Mono<Long> count = employeeRepository.count();

        return count.map(aLong -> Map.of("totalEmployees", String.valueOf(aLong)));
    }

    @Override
    public Mono<EmployeeDTO> getEmployeeByName(String name) {

        return employeeRepository.findEmployeeByName(name).map(employee -> modelMapper.map(employee, EmployeeDTO.class)).switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<EmployeeDTO> getAllEmployeesBySort(Sort.Direction sortDirection, EmployeeSortField sortField) {

        return employeeRepository.findAll(Sort.by(sortDirection, sortField.getFieldValue()))
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class)).switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<PageImpl<EmployeeDTO>> getAllEmployeesByPage(int pageNumber, int sizePerPage, Sort.Direction sortDirection, EmployeeSortField sortField) {

        Pageable pageRequest = PageRequest.of(pageNumber, sizePerPage, sortDirection, sortField.getFieldValue());

        return employeeRepository.findAllBy(pageRequest).collectList().zipWith(employeeRepository.count())
                .map(page -> new PageImpl<>(page.getT1().stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList(), pageRequest, page.getT2()))
                .switchIfEmpty(Mono.empty());
    }

}
