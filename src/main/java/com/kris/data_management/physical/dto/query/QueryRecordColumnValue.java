package com.kris.data_management.physical.dto.query;

public class QueryRecordColumnValue {
    private String stringValue;

    public QueryRecordColumnValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
