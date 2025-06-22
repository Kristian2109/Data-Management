package com.kris.data_management.controllers;

import com.kris.data_management.common.AddRecordsBatchDto;
import com.kris.data_management.common.CreateColumnDto;
import com.kris.data_management.common.CreateRecordDto;
import com.kris.data_management.common.CreateTableDto;
import com.kris.data_management.logical.query.Query;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.TableMetadata;
import com.kris.data_management.services.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<TableMetadata> createTable(@RequestBody CreateTableDto tableDto) {
        TableMetadata result = tableService.createTable(tableDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{tableId}/columns")
    public ResponseEntity<ColumnMetadata> createColumn(@PathVariable Long tableId,
            @RequestBody CreateColumnDto columnDto) {
        ColumnMetadata result = tableService.createColumn(tableId, columnDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<TableMetadata>> getTables() {
        return ResponseEntity.ok(tableService.getTablesForDatabase());
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<TableMetadata> get(@PathVariable Long tableId) {
        return ResponseEntity.ok(tableService.getById(tableId));
    }

    @PostMapping("/{tableId}/records")
    public ResponseEntity<?> addRecord(@PathVariable Long tableId, @RequestBody Map<Long, String> valuePerColumnId) {
        tableService.addRecord(tableId, valuePerColumnId);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{tableId}/records/batch")
    public ResponseEntity<?> addRecords(@PathVariable Long tableId, @RequestBody AddRecordsBatchDto recordsBatch) {
        tableService.addRecordsBatch(tableId, recordsBatch.columnIds(), recordsBatch.records());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{tableId}/query")
    public ResponseEntity<?> executeQuery(@PathVariable Long tableId, @RequestBody Query query) {
        return ResponseEntity.status(501).body("Not implemented");
    }
}