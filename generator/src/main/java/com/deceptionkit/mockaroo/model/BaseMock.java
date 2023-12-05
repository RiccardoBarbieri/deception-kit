package com.deceptionkit.mockaroo.model;

import com.deceptionkit.mockaroo.MockarooApi;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public abstract class BaseMock<T> {

    protected ArrayNode schema;

    public abstract List<T> getMocks(MockarooApi api, int count);

    public ArrayNode getSchema() {
        return this.schema;
    }
}
