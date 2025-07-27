package com.kris.data_management.controllers;

import com.kris.data_management.logical.table.CreateDefaultRelationshipDto;
import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.services.RelationshipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relationships")
public class RelationshipsController {

    private final RelationshipService relationshipService;

    public RelationshipsController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }


    @PostMapping
    public ResponseEntity<Relationship> createRelationship(@RequestBody CreateRelationshipDto createRelationshipDto) {
        return ResponseEntity.status(201).body(relationshipService.createRelationship(createRelationshipDto));
    }

    @GetMapping
    public ResponseEntity<List<Relationship>> getAllRelationships() {
        return ResponseEntity.ok(relationshipService.getAllRelationships());
    }

    @PostMapping("/default")
    public ResponseEntity<Relationship> createDefaultRelationship(@RequestBody @Valid CreateDefaultRelationshipDto dto) {
        return ResponseEntity.status(201).body(relationshipService.createDefaultRelationship(dto));
    }
}
