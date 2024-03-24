package com.vedha.webflux.repository;

import com.vedha.webflux.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
@AutoConfigureDataMongo
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("JUnit Test For Save Employee")
    public void givenEmployee_whenSave_thenReturnEmployee() {

        // given - pre-condition or setup data
        Employee vedha = Employee.builder().name("test").email("test@gmail.com").build();

        // when - action or the behaviour that we are going to test
        Publisher<Employee> save = employeeRepository.deleteAll().thenMany(employeeRepository.save(vedha));

        // then - verify the output
        StepVerifier.create(save)
                .assertNext(employee -> {
                    Assertions.assertThat(employee).isNotNull();
                    Assertions.assertThat(employee.getId()).isNotNull();
                    Assertions.assertThat(employee.getName()).isEqualTo("test");
                    Assertions.assertThat(employee.getEmail()).isEqualTo("test@gmail.com");
                }).verifyComplete();

    }


}
