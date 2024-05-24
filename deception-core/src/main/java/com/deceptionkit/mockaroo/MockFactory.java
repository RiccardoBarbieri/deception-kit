package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.model.idprovider.GroupMock;
import com.deceptionkit.mockaroo.model.idprovider.UserMock;
import com.deceptionkit.mockaroo.utils.TableRequestBuilder;
import com.deceptionkit.model.idprovider.Group;
import com.deceptionkit.model.idprovider.User;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public class MockFactory {

    private final static MockarooApi api = new MockarooApi();


    private MockFactory() {

    }

    public static List<Group> getGroups(int count) {
        GroupMock groupMock = new GroupMock();
        return groupMock.getMocks(api, count);
    }

    public static List<User> getUsers(int count, int credentialsCount, String domain) {
        UserMock userMock = new UserMock(credentialsCount, domain);
        return userMock.getMocks(api, count);
    }

    public static ArrayNode getTableTypes(String prompt, int fieldsCount) {
        return api.generateTableTypes(prompt, fieldsCount);
    }

    public static String getTableDefinition(ArrayNode schema, String tableName) {
        TableRequestBuilder builder = new TableRequestBuilder(tableName);
        for (int i = 0; i < schema.size(); i++) {
            builder.addColumn(schema.get(i).get("name").asText(), schema.get(i).get("type_id").asInt());
        }
        return api.getTableDefinition(builder.getSchema());
    }

    public static void main(String[] args) {
        ArrayNode schema = getTableTypes("warehouse inventory tracking", 5);

        System.out.println(getTableDefinition(schema, "warehouse_inventory_tracking"));
    }
}
