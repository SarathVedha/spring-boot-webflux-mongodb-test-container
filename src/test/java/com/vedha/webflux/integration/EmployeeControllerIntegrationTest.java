package com.vedha.webflux.integration;

import com.vedha.webflux.dto.EmployeeDTO;
import com.vedha.webflux.entity.Employee;
import com.vedha.webflux.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Loads the complete Spring application context but with a random port.
//@Testcontainers // Enable test containers
public class EmployeeControllerIntegrationTest extends AbstractContainerBase {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

//    @Container // Test container annotation
//    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest"); // MongoDB container

//    @DynamicPropertySource // Dynamic property source annotation
//    static void mongoDbProperties(DynamicPropertyRegistry registry) {

//        mongoDBContainer.start(); // Start the MongoDB container
//        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl); // Set the MongoDB URL to the Spring data MongoDB URI
//    }


    @BeforeEach
    public void before() {

        System.out.println("MongoDB Image Name: " + mongoDBContainer.getImage());
        System.out.println("MongoDB Docker URL: " + mongoDBContainer.getReplicaSetUrl()); // Get the MongoDB URL from the container and database name test by default
        employeeRepository.deleteAll().block(); // Delete all the data from the repository and block until it is done.
    }

    @Test
    @DisplayName("Junit Test createEmployee - Integration Test")
    public void givenEmployee_whenCreate_thenReturnEmployee() {

        // given - pre-condition or setup data
        EmployeeDTO employeeDTO = EmployeeDTO.builder().name("Test").email("test@gmail.com").build();

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
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(employeeDTO.getName()).
                jsonPath("$.email").isEqualTo(employeeDTO.getEmail())
        ;

    }

    @Test
    @DisplayName("JUnit Test getEmployeeById - Integration Test")
    public void givenEmployeeId_whenFindById_thenReturnEmployee() {

        // given - pre-condition or setup data
        Employee test = Employee.builder().name("Test").email("test@gmail.com").build();
        employeeRepository.save(test).block();

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/getById").queryParam("employeeId", test.getId()).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(test.getId())
                .jsonPath("$.name").isEqualTo(test.getName()).
                jsonPath("$.email").isEqualTo(test.getEmail())
        ;

    }

    @Test
    @DisplayName("Junit Test gstAllEmployee - Integration Test")
    public void givenListEmployee_whenGetAllEmployee_thenListEmployee() {

        // given - pre-condition or setup data
        List<Employee> build = List.of(
                Employee.builder().name("Test1").email("test1@gmail.com").build(),
                Employee.builder().name("Test2").email("test2@gmail.com").build()
        );
        employeeRepository.saveAll(build).collectList().block();

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.get().uri("/api/employees/getAll").accept(MediaType.APPLICATION_JSON).exchange();

        // then - verify the output
        exchange.expectStatus().isOk().expectBodyList(EmployeeDTO.class).consumeWith(System.out::println)
                .hasSize(2)
        ;

    }

    @Test
    @DisplayName("Junit Test updateEmployee - Integration Test")
    public void givenUpdatedEmployee_whenUpdate_thenReturnUpdatedEmployee() {

        // given - pre-condition or setup data
        Employee saved = Employee.builder().name("Test").email("test@gmail.com").build();
        employeeRepository.save(saved).block();

        EmployeeDTO updated = EmployeeDTO.builder().name("updated").email("updated@gmail.com").build();

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/updateById").queryParam("employeeId", saved.getId()).build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updated), EmployeeDTO.class)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(saved.getId())
                .jsonPath("$.name").isEqualTo(updated.getName()).
                jsonPath("$.email").isEqualTo(updated.getEmail())
        ;

    }

    @Test
    @DisplayName("JUnit Test deleteEmployee - Integration Test")
    public void givenEmployeeId_whenDelete_thenReturnCount() {

        // given - pre-condition or setup data
        Employee saved = Employee.builder().name("Test").email("test@gmail.com").build();
        employeeRepository.save(saved).block();

        // when - action or the behaviour that we are going to test
        WebTestClient.ResponseSpec exchange = webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/employees/deleteById").queryParam("employeeId", saved.getId()).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
        ;

        // then - verify the output
        exchange.expectStatus().isOk()
                .expectBody().consumeWith(System.out::println)
                .jsonPath("$.deleteCount").isEqualTo(1)
                .jsonPath("$.status").isEqualTo("deleted")
        ;

    }

}
