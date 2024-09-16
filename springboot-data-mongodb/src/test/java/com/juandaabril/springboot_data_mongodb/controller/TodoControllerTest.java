package com.juandaabril.springboot_data_mongodb.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
@Testcontainers
class TodoControllerTest {


    @Container
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:latest"));
    }

}