package com.vedha.webflux.repository;

import com.vedha.webflux.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * EmployeeRepository
 * ReactiveCrudRepository is a Spring Data interface for generic CRUD operations on WebFlux repositories.
 * It provides basic methods to save, delete, find, and count entities.
 * It also provides the ability to derive queries from the method name.
 * The EmployeeRepository interface extends the ReactiveCrudRepository interface and provides the Employee entity and the primary key type as String.
 * MongoDB uses a String type for the primary key.
 * Mono and Flux are the reactive types provided by Project Reactor.
 * All Methods in ReactiveCrudRepository are asynchronous and return Mono or Flux.
 */
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String>{

    Mono<Long> deleteEmployeeById(String id);
}
