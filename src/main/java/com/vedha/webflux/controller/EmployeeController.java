package com.vedha.webflux.controller;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.service.EmployeeService;
import com.vedha.webflux.utill.EmployeeSortField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee API")
public class EmployeeController {

    private final EmployeeService employeeService;

    // save employee reactive endpoint
    @Operation(summary = "Create Employee", description = "Create a new employee")
    @ApiResponse(responseCode = "201", description = "HTTP Status 201 OK")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.saveEmployee(employeeDTO));
    }

    // getBy Id employee reactive endpoint
    @Operation(summary = "Get Employee", description = "Get an employee by ID")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/getById", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<EmployeeDTO>> getEmployeeById(@RequestParam("employeeId") String employeeId) {

        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    // get all employees reactive endpoint
    @Operation(summary = "Get All Employees", description = "Get all employees")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/getAll", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<EmployeeDTO>> getAllEmployees() {

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // update employee reactive endpoint
    @Operation(summary = "Update Employee", description = "Update an employee by ID")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @PutMapping(value = "/updateById", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<EmployeeDTO>> updateEmployee(@RequestParam("employeeId") String employeeId, @RequestBody EmployeeDTO employeeDTO) {

        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employeeDTO));
    }

    // delete employee reactive endpoint
    @Operation(summary = "Delete Employee", description = "Delete an employee by ID")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @DeleteMapping(value = "/deleteById", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<Map<String, String>>> deleteEmployee(@RequestParam("employeeId") String employeeId) {

        return ResponseEntity.ok(employeeService.deleteEmployee(employeeId));
    }

    // get employee by name reactive endpoint
    @Operation(summary = "Get Employee By Name", description = "Get an employee by name")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/getByName", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<EmployeeDTO>> getEmployeeByName(@RequestParam("name") String name) {

        return ResponseEntity.ok(employeeService.getEmployeeByName(name));
    }

    // get total employees reactive endpoint
    @Operation(summary = "Total Employees", description = "Get total number of employees")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/total", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<Map<String, String>>> totalEmployees() {

        return ResponseEntity.ok(employeeService.totalEmployees());
    }

    @Operation(summary = "Get All Employees By Sort", description = "Get all employees by sort")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/getAllBySort", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<EmployeeDTO>> getAllEmployeesBySort(@RequestParam(value = "direction", defaultValue = "ASC") Sort.Direction sortDirection,
                                                                  @RequestParam(value = "sortBy", defaultValue = "ID") EmployeeSortField employeeSortField) {

        return ResponseEntity.ok(employeeService.getAllEmployeesBySort(sortDirection, employeeSortField));
    }

    // get all employees by page reactive endpoint
    @Operation(summary = "Get All Employees By Page", description = "Get all employees by page")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 OK")
    @GetMapping(value = "/getAllByPage", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PageImpl<EmployeeDTO>>> getAllEmployeesByPage(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                                                             @RequestParam(value = "pageSize", defaultValue = "5") int sizePerPage,
                                                                             @RequestParam(value = "direction", defaultValue = "DESC") Sort.Direction sortDirection,
                                                                             @RequestParam(value = "sortBy", defaultValue = "ID") EmployeeSortField employeeSortField) {

        return ResponseEntity.ok(employeeService.getAllEmployeesByPage(pageNumber, sizePerPage, sortDirection, employeeSortField));
    }

}
