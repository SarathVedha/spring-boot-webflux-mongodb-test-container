package com.vedha.webflux.controller;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.service.EmployeeService;
import com.vedha.webflux.utill.EmployeeSortField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
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
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id("1234567890").name("Test").email("test@gmail.com").age(25).dob(LocalDate.now().minusYears(25)).build();
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
                .jsonPath("$.age").isEqualTo(employeeDTO.getAge())
                .jsonPath("$.dob").isEqualTo(employeeDTO.getDob().toString())
        ;

    }

    @Test
    @DisplayName("Junit Test Get Employee By Id Api")
    public void givenEmployee_whenFindById_thenReturnEmployee() {

        // given - pre-condition or setup data
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id("1234567890").name("Test").email("test@gmail.com").age(25).dob(LocalDate.now().minusYears(25)).build();
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
                .jsonPath("$.age").isEqualTo(employeeDTO.getAge())
                .jsonPath("$.dob").isEqualTo(employeeDTO.getDob().toString())
        ;

    }

    @Test
    @DisplayName("Junit Test Get All Employees Api")
    public void givenListOfEmployee_whenGetAll_thenReturnListEmployee() {

        // given - pre-condition or setup data
        List<EmployeeDTO> vedha = List.of(
                EmployeeDTO.builder()
                        .id("1234567890").name("Test1").email("test1@gmail.com").age(25).dob(LocalDate.now().minusYears(25)).build(),
                EmployeeDTO.builder()
                        .id("1234567891").name("Test2").email("test2@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build()
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
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id("1234567890").name("Test").email("test@gmailcom").age(25).dob(LocalDate.now().minusYears(25)).build();
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
                .jsonPath("$.age").isEqualTo(employeeDTO.getAge())
                .jsonPath("$.dob").isEqualTo(employeeDTO.getDob().toString())
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

    @Test
    @DisplayName("JUnit Test Get Employee By Name Api")
    public void givenEmployeeName_whenFindByName_thenEmployee() {

        // given - pre-condition or setup data
        String name = "Test";
        EmployeeDTO build = EmployeeDTO.builder()
                .id("1234567890").name(name).email("test@gmail.com").age(22).dob(LocalDate.now().minusYears(22)).build();
        BDDMockito.given(employeeService.getEmployeeByName(name)).willReturn(Mono.just(build));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/getByName").queryParam("name", name).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        exchange.expectStatus().isOk().expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(build.getId())
                .jsonPath("$.name").isEqualTo(build.getName())
                .jsonPath("$.email").isEqualTo(build.getEmail())
                .jsonPath("$.age").isEqualTo(build.getAge())
                .jsonPath("$.dob").isEqualTo(build.getDob().toString());
    }

    @Test
    @DisplayName("JUnit Test For Total Employees Api")
    public void given_whenTotal_thenTotalEmployee() {

        // given - pre-condition or setup data
        BDDMockito.given(employeeService.totalEmployees()).willReturn(Mono.just(Map.of("totalEmployees", "1")));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri("/api/employees/total")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        exchange.expectStatus().isOk().expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.totalEmployees").isEqualTo("1");
    }

    @Test
    @DisplayName("JUnit Test For Sorting Employees Api")
    public void givenSort_whenEmployeeBySort_thenSortedEmployee() {

        // given - pre-condition or setup data
        List<EmployeeDTO> vedha = List.of(
                EmployeeDTO.builder()
                        .id("1234567890").name("Test1").email("test1@gmail.com").age(25).dob(LocalDate.now().minusYears(25)).build(),
                EmployeeDTO.builder()
                        .id("1234567891").name("Test2").email("test2@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567892").name("Test3").email("test3@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567893").name("Test4").email("test4@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567894").name("Test5").email("test5@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build()
        );

        Sort.Direction desc = Sort.Direction.DESC;
        EmployeeSortField id = EmployeeSortField.ID;

        BDDMockito.given(employeeService.getAllEmployeesBySort(desc, id)).willReturn(Flux.fromIterable(vedha));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/employees/getAllBySort")
                        .queryParam("direction", desc)
                        .queryParam("sortBy", id)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        exchange.expectStatus().isOk().expectBodyList(EmployeeDTO.class)
                .consumeWith(System.out::println)
                .hasSize(5);
    }

    @Test
    @DisplayName("JUnit Test For Pagination Employees Api")
    public void givenPagination_whenFindAll_thenReturnPaginationEmployee() {

        // given - pre-condition or setup data
        List<EmployeeDTO> vedha = List.of(
                EmployeeDTO.builder()
                        .id("1234567890").name("Test1").email("test1@gmail.com").age(25).dob(LocalDate.now().minusYears(25)).build(),
                EmployeeDTO.builder()
                        .id("1234567891").name("Test2").email("test2@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567892").name("Test3").email("test3@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567893").name("Test4").email("test4@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build(),
                EmployeeDTO.builder()
                        .id("1234567894").name("Test5").email("test5@gmail.com").age(26).dob(LocalDate.now().minusYears(26)).build()
        );

        int page = 0;
        int pageSize = 3;
        Sort.Direction desc = Sort.Direction.DESC;
        EmployeeSortField id = EmployeeSortField.ID;

        BDDMockito.given(employeeService.getAllEmployeesByPage(page, pageSize, desc, id))
                .willReturn(Mono.just(new PageImpl<>(vedha, PageRequest.of(page, pageSize, desc, id.getFieldValue()), vedha.size())));

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get().uri(uriBuilder -> uriBuilder.path("/api/employees/getAllByPage")
                        .queryParam("page", page)
                        .queryParam("pageSize", pageSize)
                        .queryParam("direction", desc)
                        .queryParam("sortBy", id)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output
        exchange.expectStatus().isOk().expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.content.size()").isEqualTo(vedha.size())
                .jsonPath("$.totalElements").isEqualTo(vedha.size());

    }

}
