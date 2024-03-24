package com.vedha.webflux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Spring Boot Webflux Testing", version = "1.0", description = "Spring Boot Webflux Testing With MongoDB"))
public class SpringBootWebFluxTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebFluxTestingApplication.class, args);
	}

}
