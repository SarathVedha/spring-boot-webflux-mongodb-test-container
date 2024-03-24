package com.vedha.webflux.controller;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@WebFluxTest(EmployeeController.class) // Loads Only Web and Controller Beans in IOC container, it's not mandatory to add controller class in the annotation
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("JUnit Test Create Employee Api")
    public void givenEmployee_whenCreate_thenReturnEmployee() {

        // given - pre-condition or setup data
        EmployeeDTO employeeDTO = EmployeeDTO.builder().id("1234567890").name("Test").email("test@gmail.com").build();
        BDDMockito.given(employeeService.saveEmployee(BDDMockito.any(EmployeeDTO.class))).willReturn(Mono.just(employeeDTO));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.post().uri("/api/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDTO), EmployeeDTO.class)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isCreated()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(employeeDTO.getId())
                .jsonPath("$.name").isEqualTo(employeeDTO.getName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail())
        ;

    }

    @Test
    @DisplayName("Junit Test Get Employee By Id Api")
    public void givenEmployee_whenFindById_thenReturnEmployee() {

        // given - pre-condition or setup data
        EmployeeDTO employeeDTO = EmployeeDTO.builder().id("1234567890").name("Test").email("test@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(employeeDTO.getId())).willReturn(Mono.just(employeeDTO));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/getById").queryParam("employeeId", employeeDTO.getId()).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(employeeDTO.getId())
                .jsonPath("$.name").isEqualTo(employeeDTO.getName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail())
        ;

    }

    @Test
    @DisplayName("Junit Test Get All Employees Api")
    public void givenListOfEmployee_whenGetAll_thenReturnListEmployee() {

        // given - pre-condition or setup data
        List<EmployeeDTO> vedha = List.of(
                EmployeeDTO.builder().id("1234567890").name("Test1").email("test1@gmail.com").build(),
                EmployeeDTO.builder().id("1234567891").name("Test2").email("test2@gmail.com").build()
        );
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(Flux.fromIterable(vedha));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get().uri("/api/employees/getAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk().expectBodyList(EmployeeDTO.class)
                .consumeWith(System.out::println)
                .hasSize(2)
//                .jsonPath("$.size()").isEqualTo(2);
        ;

    }

    @Test
    @DisplayName("Junit Test Update Employee Api")
    public void givenUpdatedEmployee_whenUpdate_thenReturnEmployee() {

        // given - pre-condition or setup data
        String employeeId = "1234567890";
        EmployeeDTO employeeDTO = EmployeeDTO.builder().id("1234567890").name("Test").email("test@gmailcom").build();
        BDDMockito.given(employeeService.updateEmployee(BDDMockito.any(String.class), BDDMockito.any(EmployeeDTO.class))).willReturn(Mono.just(employeeDTO));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/updateById").queryParam("employeeId", employeeId).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDTO), EmployeeDTO.class)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk().expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(employeeId)
                .jsonPath("$.name").isEqualTo(employeeDTO.getName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail())
        ;

    }

    @Test
    @DisplayName("JUnit Test Delete Employee Api")
    public void givenEmployeeId_whenDelete_thenReturnCount() {

        // given - pre-condition or setup data
        String employeeId = "123456";
        Map<String, String> deleteCount = Map.of("deleteCount", "1", "status", "deleted");

        BDDMockito.given(employeeService.deleteEmployee(employeeId)).willReturn(Mono.just(deleteCount));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/deleteById").queryParam("employeeId", employeeId).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk().expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.deleteCount").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("deleted")
        ;

    }

}
