package com.deceptionkit.mockaroo.model.database;

import com.deceptionkit.mockaroo.MockarooApi;
import com.deceptionkit.mockaroo.model.BaseMock;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public class GenericMockSql extends BaseMock<String> {

    private final String tableName;

    public GenericMockSql(ArrayNode schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;


    }

    @Override
    public List<String> getMocks(MockarooApi api, int count) {
        return api.genMockSql(this.schema, count, tableName);
    }
}
