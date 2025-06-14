package com.kris.data_management.services;

import com.kris.data_management.dto.CreateTableDto;
import com.kris.data_management.repositories.TableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void createTable(CreateTableDto request) {
        tableRepository.createTable(request);
    }
} 