package com.deceptionkit.yamlspecs.database.table;

import java.util.List;

public class TableSpecification {

    private Integer min_columns_per_table;
    private Integer max_columns_per_table;
    private Integer min_rows_per_table;
    private Integer max_rows_per_table;
    private List<TableDefinition> definitions;

    public Integer getMin_columns_per_table() {
        return min_columns_per_table;
    }

    public void setMin_columns_per_table(Integer min_columns_per_table) {
        this.min_columns_per_table = min_columns_per_table;
    }

    public Integer getMax_columns_per_table() {
        return max_columns_per_table;
    }

    public void setMax_columns_per_table(Integer max_columns_per_table) {
        this.max_columns_per_table = max_columns_per_table;
    }

    public Integer getMin_rows_per_table() {
        return min_rows_per_table;
    }

    public void setMin_rows_per_table(Integer min_rows_per_table) {
        this.min_rows_per_table = min_rows_per_table;
    }

    public Integer getMax_rows_per_table() {
        return max_rows_per_table;
    }

    public void setMax_rows_per_table(Integer max_rows_per_table) {
        this.max_rows_per_table = max_rows_per_table;
    }

    public List<TableDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<TableDefinition> definitions) {
        this.definitions = definitions;
    }
}
