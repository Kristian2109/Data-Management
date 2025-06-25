package com.kris.data_management.physical.dto;

public enum DatabaseColumnType {
    INT("INT"),
    VARCHAR_255("VARCHAR(255)"),
    TEXT("TEXT"),
    BIGINT("BIGINT"),
    FOREIGN_KEY("BIGINT");

    private final String sqlType;

    DatabaseColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType() {
        return sqlType;
    }
}