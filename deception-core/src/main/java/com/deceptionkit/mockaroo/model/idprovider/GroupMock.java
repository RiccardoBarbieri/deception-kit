package com.deceptionkit.mockaroo.model.idprovider;

import com.deceptionkit.mockaroo.MockarooApi;
import com.deceptionkit.mockaroo.model.BaseMock;
import com.deceptionkit.model.idprovider.Group;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class GroupMock extends BaseMock<Group> {

    private final ObjectNode name =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "name")
                    .put("type", "Department (Corporate)");

    public GroupMock() {
        this.schema = (new ObjectMapper()).createArrayNode();
        this.schema.add(name);
    }

    @Override
    public List<Group> getMocks(MockarooApi api, int count) {
        List<Group> groups = new ArrayList<>();
        ArrayNode jsonGroups = api.genMockJson(this.getSchema(), count);
        for (int i = 0; i < count; i++) {
            Group group = new Group();
            group.setName(jsonGroups.get(i).get("name").asText());
            group.setRoles(null);
            groups.add(group);
        }
        return groups;
    }

}
