package com.kris.data_management.dtos.model.table;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColumnDataType {
    NUMBER("number"),
    TEXT("text"),
    LONG_TEXT("long-text"),
    FOREIGN_KEY("foreign_key");

    private final String type;

    ColumnDataType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}