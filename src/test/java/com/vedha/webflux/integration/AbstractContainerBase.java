package com.vedha.webflux.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public abstract class AbstractContainerBase {

    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest"); // MongoDB container

    static {

        mongoDBContainer.start(); // Start the MongoDB container
        System.out.println("Container Info: " + mongoDBContainer.getContainerInfo());
    }

    @DynamicPropertySource
    private static void dynamicPropertySource(DynamicPropertyRegistry registry) {

        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl); // Set the MongoDB URL to the Spring data MongoDB URI
    }

}
