package com.kris.data_management.controllers;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.kris.data_management.BaseTest;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class TableControllerTest extends BaseTest {

    private String dbPhysicalName;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // Create a fresh database for each test
        dbPhysicalName = createTestDatabase("Test-DB-For-Tables");
    }

    @Nested
    @DisplayName("Table Creation Tests")
    public class CreateTableTests {

        @Test
        @DisplayName("Should create a table with all basic column types")
        void shouldCreateTableWithAllColumnTypes() {
            // Arrange
            List<Map<String, Object>> columns = List.of(
                Map.of("displayName", "Name", "type", "text"),
                Map.of("displayName", "Age", "type", "number"),
                Map.of("displayName", "Bio", "type", "long_text")
            );

            Map<String, Object> requestBody = Map.of(
                "displayName", "Users",
                "columns", columns
            );

            // Act & Assert
            given()
                .header("X-Database-Name", dbPhysicalName)
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/tables")
            .then()
                .statusCode(201)
                .body("displayName", is("Users"))
                .body("columns", hasSize(4)) // 3 custom + 1 auto-created 'id' column
                .body("columns.find { it.displayName == 'Name' }.type", is("text"))
                .body("columns.find { it.displayName == 'Age' }.type", is("number"))
                .body("columns.find { it.displayName == 'Bio' }.type", is("long_text"));
        }
    }

    @Nested
    @DisplayName("Tests on an Existing Table")
    public class ExistingTableTests {

        private String tablePhysicalName;
        private String initialColumnPhysicalName;

        @BeforeEach
        public void setUp() {
            // Create a standard table to run tests against
            Map<String, Object> column = Map.of("displayName", "InitialColumn", "type", "text");
            Map<String, Object> requestBody = Map.of("displayName", "StandardTable", "columns", List.of(column));

            // First, create the table and get its physical name
            tablePhysicalName = given()
                .header("X-Database-Name", dbPhysicalName)
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/tables")
            .then()
                .statusCode(201)
                .extract()
                .path("physicalName");

            // Then, get the table's details to find the physical name of our column
            initialColumnPhysicalName = given()
                .header("X-Database-Name", dbPhysicalName)
            .when()
                .get("/tables/{tableName}", tablePhysicalName)
            .then()
                .statusCode(200)
                .extract()
                .path("columns.find { it.displayName == 'InitialColumn' }.physicalName");
        }

        @Test
        @DisplayName("Should add a new column to an existing table")
        void shouldAddColumn() {
            // Arrange
            Map<String, Object> newColumnBody = Map.of("displayName", "NewColumn", "type", "number");

            // Act & Assert
            given()
                .header("X-Database-Name", dbPhysicalName)
                .contentType(ContentType.JSON)
                .body(newColumnBody)
            .when()
                .post("/tables/{tableName}/columns", tablePhysicalName)
            .then()
                .statusCode(201)
                .body("displayName", is("NewColumn"))
                .body("type", is("number"));
        }

        @Test
        @DisplayName("Should add a new record to an existing table")
        void shouldAddRecord() {
            // Arrange
            List<Map<String, String>> recordValues = List.of(
                Map.of("columnName", initialColumnPhysicalName, "stringValue", "Some Value")
            );
            Map<String, Object> requestBody = Map.of("columnValues", recordValues);

            // Act & Assert
            given()
                .header("X-Database-Name", dbPhysicalName)
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/tables/{tableName}/records", tablePhysicalName)
            .then()
                .statusCode(201);
        }
    }
} 