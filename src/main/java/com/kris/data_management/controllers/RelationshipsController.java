package com.kris.data_management.controllers;

import com.kris.data_management.logical.table.CreateRelationshipDto;
import com.kris.data_management.logical.table.Relationship;
import com.kris.data_management.services.RelationshipService;
import com.kris.data_management.services.TableService;
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
}
