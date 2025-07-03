package com.kris.data_management.physical.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ColumnDataType {
    NUMBER("number"),
    TEXT("text"),
    LONG_TEXT("long_text"),
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