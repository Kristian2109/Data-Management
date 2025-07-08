package com.kris.data_management.logical.repository.relationship;

import com.kris.data_management.logical.table.ColumnMetadata;
import com.kris.data_management.logical.table.FullTableMetadata;
import com.kris.data_management.logical.table.Relationship;

public interface RelationshipRepository {
    Relationship getById(Long id);
    Relationship create(String name, FullTableMetadata parentTable, FullTableMetadata childTable, String parentColumnName, String childColumnName);
}
