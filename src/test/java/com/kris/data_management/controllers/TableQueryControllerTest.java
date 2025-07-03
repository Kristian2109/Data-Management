package com.kris.data_management.controllers;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kris.data_management.BaseTest;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@DisplayName("Table Query Tests")
public class TableQueryControllerTest extends BaseTest {

    private String dbPhysicalName;
    private String tablePhysicalName;
    private String nameColumn;
    private String priceColumn;
    private String categoryColumn;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // 1. Create a database
        dbPhysicalName = createTestDatabase("Query-Test-DB");

        // 2. Create the table schema
        List<Map<String, Object>> columns = List.of(
            Map.of("displayName", "Name", "type", "text"),
            Map.of("displayName", "Price", "type", "number"),
            Map.of("displayName", "Category", "type", "text")
        );
        Map<String, Object> tableBody = Map.of("displayName", "Products", "columns", columns);

        tablePhysicalName = given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(tableBody)
        .when().post("/tables")
        .then().statusCode(201).extract().path("physicalName");

        // 3. Get the physical column names
        Map<String, String> columnNames = given()
            .header("X-Database-Name", dbPhysicalName)
        .when().get("/tables/{tableName}", tablePhysicalName)
        .then().statusCode(200).extract().path("columns.collectEntries { [ (it.displayName): it.physicalName ] }");
        
        nameColumn = columnNames.get("Name");
        priceColumn = columnNames.get("Price");
        categoryColumn = columnNames.get("Category");

        // 4. Populate the table with data
        Map<String, Object> batchBody = Map.of(
            "columnNames", List.of(nameColumn, priceColumn, categoryColumn),
            "records", List.of(
                List.of("Laptop", "1200", "Electronics"),
                List.of("Mouse", "25", "Electronics"),
                List.of("Desk", "300", "Furniture"),
                List.of("Chair", "150", "Furniture")
            )
        );

        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(batchBody)
        .when().post("/tables/{tableName}/records/batch", tablePhysicalName)
        .then().statusCode(201);
    }
    
    private Map<String, Object> createQueryBody(List<Map<String, Object>> selects, List<Map<String, Object>> filters, List<Map<String, Object>> orders, Map<String, Object> pagination) {
        return Map.of(
            "select", selects,
            "filters", filters,
            "orders", orders,
            "pagination", pagination,
            "joins", List.of()
        );
    }

    @Test
    @DisplayName("Should perform a simple query to get all records")
    void shouldPerformSimpleQuery() {
        // Arrange
        List<Map<String, Object>> selects = List.of(
            Map.of("columnName", nameColumn, "tableName", tablePhysicalName)
        );
        Map<String, Object> pagination = Map.of("pageNumber", 0, "pageSize", 10);
        Map<String, Object> queryBody = createQueryBody(selects, List.of(), List.of(), pagination);

        // Act & Assert
        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(queryBody)
        .when().post("/tables/{tableName}/query", tablePhysicalName)
        .then()
            .statusCode(200)
            .body("records", hasSize(4))
            .body("totalRecords", is(4));
    }

    @Test
    @DisplayName("Should query with pagination")
    void shouldQueryWithPagination() {
        // Arrange
        List<Map<String, Object>> selects = List.of(
            Map.of("columnName", nameColumn, "tableName", tablePhysicalName)
        );
        // Get the second page, with 2 items per page
        Map<String, Object> pagination = Map.of("pageNumber", 1, "pageSize", 2);
        List<Map<String, Object>> orders = List.of(
            Map.of("columnName", priceColumn, "tableName", tablePhysicalName, "direction", "ASC")
        );
        Map<String, Object> queryBody = createQueryBody(selects, List.of(), orders, pagination);

        // Act & Assert
        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(queryBody)
        .when().post("/tables/{tableName}/query", tablePhysicalName)
        .then()
            .statusCode(200)
            .body("records", hasSize(2))
            .body("totalRecords", is(4));
    }

    @Test
    @DisplayName("Should query with a filter")
    void shouldQueryWithFilter() {
        // Arrange
        List<Map<String, Object>> selects = List.of(
            Map.of("columnName", nameColumn, "tableName", tablePhysicalName)
        );
        List<Map<String, Object>> filters = List.of(
            Map.of("columnName", categoryColumn, "tableName", tablePhysicalName, "operator", "EQUAL", "value", "Electronics")
        );
        Map<String, Object> pagination = Map.of("pageNumber", 0, "pageSize", 10);
        Map<String, Object> queryBody = createQueryBody(selects, filters, List.of(), pagination);

        // Act & Assert
        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(queryBody)
        .when().post("/tables/{tableName}/query", tablePhysicalName)
        .then()
            .statusCode(200)
            .body("records", hasSize(2))
            .body("records.columnValues.flatten().collect { it.stringValue }", containsInAnyOrder("Laptop", "Mouse"));
    }

    @Test
    @DisplayName("Should query with ordering")
    void shouldQueryWithOrdering() {
        // Arrange
        List<Map<String, Object>> selects = List.of(
            Map.of("columnName", nameColumn, "tableName", tablePhysicalName)
        );
        List<Map<String, Object>> orders = List.of(
            Map.of("columnName", priceColumn, "tableName", tablePhysicalName, "direction", "DESC")
        );
        Map<String, Object> pagination = Map.of("pageNumber", 0, "pageSize", 10);
        Map<String, Object> queryBody = createQueryBody(selects, List.of(), orders, pagination);

        // Act & Assert
        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(queryBody)
        .when().post("/tables/{tableName}/query", tablePhysicalName)
        .then()
            .statusCode(200)
            .body("records", hasSize(4))
            .body("records[0].columnValues[0].stringValue", is("Laptop")) // Most expensive
            .body("records[3].columnValues[0].stringValue", is("Mouse"));   // Least expensive
    }
} 