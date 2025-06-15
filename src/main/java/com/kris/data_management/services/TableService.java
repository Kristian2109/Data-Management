package com.kris.data_management.services;

import com.kris.data_management.physical.dto.CreateTableDto;
import com.kris.data_management.physical.repository.PhysicalTableRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class TableService {
    private static final String DB_PREFIX = "db";
    private static final Integer RANDOM_PART_SIZE = 5;

    private final PhysicalTableRepositoryImpl tableRepository;

    public TableService(PhysicalTableRepositoryImpl tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void createTable(CreateTableDto request) {
        tableRepository.createTable(request);
    }
} 