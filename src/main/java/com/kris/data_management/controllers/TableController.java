package com.kris.data_management.controllers;

import com.kris.data_management.physical.dto.CreateTableDto;
import com.kris.data_management.services.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<String> createTable(@RequestBody CreateTableDto createTableDto) {
        tableService.createTable(createTableDto);
        return ResponseEntity.ok("Table '" + createTableDto.getTableName() + "' created successfully in the specified database.");
    }
} 