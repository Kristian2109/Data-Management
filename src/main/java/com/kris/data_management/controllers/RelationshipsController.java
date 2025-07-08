package com.kris.data_management.controllers;

import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.services.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relationships")
public class RelationshipsController {

    private final TableService tableService;

    public RelationshipsController(TableService tableService) {
        this.tableService = tableService;
    }


    @PostMapping
    public ResponseEntity<Relationship> createRelationship(@RequestBody CreateRelationshipDto createRelationshipDto) {
        return ResponseEntity.ok(tableService.createRelationship(createRelationshipDto));
    }
}
