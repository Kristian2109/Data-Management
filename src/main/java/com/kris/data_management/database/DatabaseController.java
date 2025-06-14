package com.kris.data_management.database;

import com.kris.data_management.entities.DatabaseEntity;
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
    public ResponseEntity<DatabaseEntity> createDatabase(@RequestBody DatabaseRequestDto request) {
        DatabaseEntity createdDatabase = databaseService.createDatabase(request.getDisplayName());
        return ResponseEntity.ok(createdDatabase);
    }
} 