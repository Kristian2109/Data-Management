package com.kris.data_management.database;

public class DatabaseContext {
    private static final ThreadLocal<String> CURRENT_DATABASE = new ThreadLocal<>();

    public static void setCurrentDatabase(String dbName) {
        CURRENT_DATABASE.set(dbName);
    }

    public static String getCurrentDatabase() {
        return CURRENT_DATABASE.get();
    }

    public static void clear() {
        CURRENT_DATABASE.remove();
    }
} 