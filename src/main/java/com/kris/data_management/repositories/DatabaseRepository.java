package com.kris.data_management.repositories;

import com.kris.data_management.entities.DatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Long> {
}
