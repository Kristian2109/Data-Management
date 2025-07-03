package com.kris.data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = DataManagementApplication.class)
@ActiveProfiles("test")
public abstract class BaseTest {

    @BeforeEach
    public void setUp() {
        RestAssured.port = 8081; // Ensure this matches the port in application-test.properties
    }

    protected String createTestDatabase(String displayName) {
        // Create a DTO for the request body
        var createDbDto = new java.util.HashMap<String, String>();
        createDbDto.put("displayName", displayName);

        // Make the request and extract the physical name
        return io.restassured.RestAssured.given()
            .contentType("application/json")
            .body(createDbDto)
        .when()
            .post("/databases")
        .then()
            .statusCode(200)
            .extract()
            .path("physicalName");
    }
} 