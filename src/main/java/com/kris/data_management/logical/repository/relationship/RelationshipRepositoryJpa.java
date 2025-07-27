package com.kris.data_management.logical.repository.relationship;

import com.kris.data_management.logical.entities.RelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationshipRepositoryJpa extends JpaRepository<RelationshipEntity, Integer> {
    @Query(value = """
    SELECT r.*
    FROM relationship_entity r
    JOIN column_metadata_entity fc ON r.from_column_id = fc.id
    JOIN column_metadata_entity tc ON r.to_column_id = tc.id
    WHERE fc.table_metadata_id = :tableId
       OR tc.table_metadata_id = :tableId
    """, nativeQuery = true)
    List<RelationshipEntity> findAllByTableIdNative(@Param("tableId") String tableId);

}
