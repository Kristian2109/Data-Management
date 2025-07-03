package com.kris.data_management.controllers;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;

import com.kris.data_management.BaseTest;

import static io.restassured.RestAssured.given;

public class DatabaseControllerTest extends BaseTest {

    @Test
    public void testCreateAndGetDatabases() {
        // 1. Create a new database
        String dbDisplayName = "My Test DB";
        createTestDatabase(dbDisplayName);

        // 2. Get all databases and verify the new one is in the list
        given()
            .when()
            .get("/databases")
            .then()
            .statusCode(200)
            .body("find { it.displayName == '" + dbDisplayName + "' }.displayName", is(dbDisplayName));
    }
} 