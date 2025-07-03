package com.kris.data_management.controllers;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kris.data_management.BaseTest;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

@DisplayName("Table Join Query Tests")
public class TableJoinQueryControllerTest extends BaseTest {

    private String dbPhysicalName;
    private String customersTable;
    private String ordersTable;
    private String customerNameCol;
    private String orderDescriptionCol;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        dbPhysicalName = createTestDatabase("Join-Query-Test-DB");

        // 1. Create Customers Table
        customersTable = createTable("Customers", List.of(
            Map.of("displayName", "Name", "type", "text"),
            Map.of("displayName", "Email", "type", "text")
        ));
        Map<String, String> customerCols = getPhysicalColumnNames(customersTable);
        customerNameCol = customerCols.get("Name");
        String customerEmailCol = customerCols.get("Email");

        // 2. Add multiple customer records
        addRecord(customersTable, Map.of(customerNameCol, "John Doe", customerEmailCol, "john.doe@example.com"));    // ID 1
        addRecord(customersTable, Map.of(customerNameCol, "Jane Smith", customerEmailCol, "jane.smith@example.com"));// ID 2
        addRecord(customersTable, Map.of(customerNameCol, "Peter Jones", customerEmailCol, "peter.jones@example.com")); // ID 3

        // 3. Create Orders Table
        ordersTable = createTable("Orders", List.of(
            Map.of("displayName", "Description", "type", "text"),
            Map.of("displayName", "Time", "type", "text")
        ));

        // 4. Add foreign key column to Orders table
        addColumn(ordersTable, "customer_id", "foreign_key", customersTable, "id");
        Map<String, String> orderCols = getPhysicalColumnNames(ordersTable);
        orderDescriptionCol = orderCols.get("Description");
        String orderTimeCol = orderCols.get("Time");
        String orderFkCol = orderCols.get("customer_id");

        // 5. Add orders linked to the customers
        addRecord(ordersTable, Map.of(orderDescriptionCol, "Laptop", orderTimeCol, "2024-01-01T10:00:00Z", orderFkCol, "1"));
        addRecord(ordersTable, Map.of(orderDescriptionCol, "Mouse", orderTimeCol, "2024-01-01T11:00:00Z", orderFkCol, "1"));
        addRecord(ordersTable, Map.of(orderDescriptionCol, "Monitor", orderTimeCol, "2024-01-02T14:00:00Z", orderFkCol, "3"));
    }

    @Test
    @DisplayName("Should join Orders with Customers to retrieve customer name for all orders")
    void shouldQueryWithJoin() {
        // Arrange: Query all orders and join customers to get the customer's name for each order
        List<Map<String, Object>> selects = List.of(
            Map.of("tableName", ordersTable, "columnName", orderDescriptionCol),
            Map.of("tableName", customersTable, "columnName", customerNameCol)
        );

        List<Map<String, Object>> joins = List.of(
            Map.of(
                "leftTableName", ordersTable,
                "leftColumnName", getPhysicalColumnNames(ordersTable).get("customer_id"),
                "rightTableName", customersTable,
                "rightColumnName", "id"
            )
        );

        // Add ordering to make assertions predictable
        List<Map<String, Object>> orders = List.of(
            Map.of("tableName", ordersTable, "columnName", "id", "direction", "ASC")
        );

        Map<String, Object> queryBody = Map.of(
            "select", selects,
            "filters", List.of(),
            "orders", orders,
            "pagination", Map.of("pageNumber", 0, "pageSize", 10),
            "joins", joins
        );

        // Act & Assert
        given()
            .header("X-Database-Name", dbPhysicalName)
            .contentType(ContentType.JSON)
            .body(queryBody)
        .when()
            .post("/tables/{tableName}/query", ordersTable)
        .then()
            .statusCode(200)
            .body("records", hasSize(3))
            // Check first order (Laptop for John Doe)
            .body("records[0].columnValues[0].stringValue", is("Laptop"))
            .body("records[0].columnValues[1].stringValue", is("John Doe"))
            // Check second order (Mouse for John Doe)
            .body("records[1].columnValues[0].stringValue", is("Mouse"))
            .body("records[1].columnValues[1].stringValue", is("John Doe"))
            // Check third order (Monitor for Peter Jones)
            .body("records[2].columnValues[0].stringValue", is("Monitor"))
            .body("records[2].columnValues[1].stringValue", is("Peter Jones"));
    }
    
    // Helper methods to reduce boilerplate
    private String createTable(String displayName, List<Map<String, Object>> columns) {
        Map<String, Object> tableBody = Map.of("displayName", displayName, "columns", columns);
        return given()
            .header("X-Database-Name", dbPhysicalName).contentType(ContentType.JSON).body(tableBody)
            .when().post("/tables")
            .then().statusCode(201).extract().path("physicalName");
    }

    private void addColumn(String tableName, String displayName, String type, String parentTable, String parentColumn) {
        Map<String, Object> columnBody = Map.of(
            "displayName", displayName,
            "type", type,
            "parent", Map.of("table", parentTable, "column", parentColumn)
        );
        given()
            .header("X-Database-Name", dbPhysicalName).contentType(ContentType.JSON).body(columnBody)
            .when().post("/tables/{tableName}/columns", tableName)
            .then().statusCode(201);
    }
    
    private void addRecord(String tableName, Map<String, String> values) {
        List<Map<String, String>> columnValues = values.entrySet().stream()
            .map(e -> Map.of("columnName", e.getKey(), "stringValue", e.getValue()))
            .toList();
        
        given()
            .header("X-Database-Name", dbPhysicalName).contentType(ContentType.JSON).body(Map.of("columnValues", columnValues))
            .when().post("/tables/{tableName}/records", tableName)
            .then().statusCode(201);
    }

    private Map<String, String> getPhysicalColumnNames(String tableName) {
        return given()
            .header("X-Database-Name", dbPhysicalName)
        .when().get("/tables/{tableName}", tableName)
        .then().statusCode(200).extract().path("columns.collectEntries { [ (it.displayName): it.physicalName ] }");
    }
} 