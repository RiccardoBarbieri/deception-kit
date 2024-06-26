package com.deceptionkit.generation.database.model;

import java.util.List;

public class TableResources {

    private String databaseName;
    private String tableName;
    private String tableCreateStm;
    private List<String> inserts;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableCreateStm() {
        return tableCreateStm;
    }

    public void setTableCreateStm(String tableCreateStm) {
        this.tableCreateStm = tableCreateStm;
    }

    public List<String> getInserts() {
        return inserts;
    }

    public void setInserts(List<String> inserts) {
        this.inserts = inserts;
    }
}
