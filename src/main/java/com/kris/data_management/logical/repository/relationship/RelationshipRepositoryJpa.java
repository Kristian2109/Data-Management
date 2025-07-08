package com.kris.data_management.logical.repository.relationship;

import com.kris.data_management.logical.entities.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepositoryJpa extends JpaRepository<RelationshipEntity, Integer> { }
