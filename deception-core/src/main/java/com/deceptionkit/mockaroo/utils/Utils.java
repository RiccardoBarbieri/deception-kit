package com.deceptionkit.mockaroo.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Utils {

    public static ArrayNode removeTypeId(ArrayNode schema) {
        ArrayNode newSchema = schema.deepCopy();
        for (int i = 0; i < schema.size(); i++) {
            ((ObjectNode) newSchema.get(i)).remove("type_id");
        }
        return newSchema;
    }
}
