package com.kris.data_management.common;

public class ColumnTypeMapper {
    public static DatabaseColumnType map(ColumnDataType type) {
        return switch (type) {
            case NUMBER -> DatabaseColumnType.INT;
            case TEXT -> DatabaseColumnType.VARCHAR_255;
            case LONG_TEXT -> DatabaseColumnType.TEXT;
            case FOREIGN_KEY -> DatabaseColumnType.FOREIGN_KEY;
        };
    }
}
