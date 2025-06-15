package com.kris.data_management.dtos.model.logicalQuery;

public class ColumnValue {
    private String stringValue;

    public ColumnValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
