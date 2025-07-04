package com.kris.data_management.controllers;

import java.util.List;

import com.kris.data_management.logical.table.BaseTableMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kris.data_management.physical.dto.AddRecordsBatchDto;
import com.kris.data_management.physical.dto.CreateColumnDto;
import com.kris.data_management.physical.dto.CreateRecordDto;
import com.kris.data_management.physical.dto.CreateTableDto;
import com.kris.data_management.logical.table.CreateTableViewDto;
import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.ViewMetadata;
import com.kris.data_management.physical.query.PhysicalQuery;
import com.kris.data_management.physical.query.QueryResult;
import com.kris.data_management.services.TableService;

@RestController
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<FullTableMetadata> createTable(@RequestBody CreateTableDto tableDto) {
        FullTableMetadata result = tableService.createTable(tableDto);
        return ResponseEntity.status(201).body(result);
    }

    @PostMapping("/{tableId}/columns")
    public ResponseEntity<ColumnMetadata> createColumn(@PathVariable String tableId,
            @RequestBody CreateColumnDto columnDto) {
        ColumnMetadata result = tableService.createColumn(tableId, columnDto);
        return ResponseEntity.status(201).body(result);
    }

    @GetMapping
    public ResponseEntity<List<BaseTableMetadata>> getTables() {
        return ResponseEntity.ok(tableService.getTablesForDatabase());
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<FullTableMetadata> get(@PathVariable String tableId) {
        return ResponseEntity.ok(tableService.getById(tableId));
    }

    @PostMapping("/{tableId}/records")
    public ResponseEntity<?> addRecord(@PathVariable String tableId, @RequestBody CreateRecordDto recordDto) {
        tableService.addRecord(tableId, recordDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{tableId}/records/batch")
    public ResponseEntity<?> addRecords(@PathVariable String tableId, @RequestBody AddRecordsBatchDto recordsBatch) {
        tableService.addRecordsBatch(tableId, recordsBatch.columnNames(), recordsBatch.records());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{tableId}/query")
    public ResponseEntity<QueryResult> executeQuery(@PathVariable String tableId, @RequestBody PhysicalQuery query) {
        return ResponseEntity.ok(tableService.queryRecords(tableId, query));
    }

    @PostMapping("/{tableName}/views")
    public ResponseEntity<ViewMetadata> createView(@PathVariable String tableName, @RequestBody CreateTableViewDto viewDto) {
        return ResponseEntity.status(201).body(tableService.createView(tableName, viewDto));
    }
}