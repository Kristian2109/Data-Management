package com.kris.data_management.controllers;

import com.kris.data_management.database.dto.CreateDatabaseDto;
import com.kris.data_management.services.DatabaseService;
import com.kris.data_management.database.entities.DatabaseMetadataEntity;
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
    public ResponseEntity<DatabaseMetadataEntity> createDatabase(@RequestBody CreateDatabaseDto request) {
        DatabaseMetadataEntity createdDatabase = databaseService.createDatabase(request);
        return ResponseEntity.ok(createdDatabase);
    }
} 