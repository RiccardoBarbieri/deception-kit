package com.deceptionkit.generation.database.model;

public class DatabaseResources {

    private String databaseName;
    private String createDatabaseStm;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCreateDatabaseStm() {
        return createDatabaseStm;
    }

    public void setCreateDatabaseStm(String createDatabaseStm) {
        this.createDatabaseStm = createDatabaseStm;
    }
}
