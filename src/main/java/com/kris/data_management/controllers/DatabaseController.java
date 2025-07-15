package com.kris.data_management.controllers;

import com.kris.data_management.database.dto.CreateDatabaseDto;
import com.kris.data_management.database.dto.DatabaseMetadata;
import com.kris.data_management.services.DatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/databases")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping
    public ResponseEntity<DatabaseMetadata> createDatabase(@RequestBody CreateDatabaseDto request) {
        DatabaseMetadata createdDatabase = databaseService.createDatabase(request);
        return ResponseEntity.ok(createdDatabase);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatabaseMetadata> getDatabase(@PathVariable String id) {
        DatabaseMetadata database = databaseService.getDatabase(id);
        return ResponseEntity.ok(database);
    }

    @GetMapping
    public ResponseEntity<java.util.List<DatabaseMetadata>> getAllDatabases() {
        return ResponseEntity.ok(databaseService.getAllDatabases());
    }
} 