package com.deceptionkit.mockaroo.model.idprovider;

import com.deceptionkit.mockaroo.MockarooApi;
import com.deceptionkit.mockaroo.model.BaseMock;
import com.deceptionkit.model.Credential;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class CredentialMock extends BaseMock<Credential> {

    private final ObjectNode value =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "value")
                    .put("type", "Password")
                    .put("min_upper", 1)
                    .put("min_lower", 5)
                    .put("min_length", 10)
                    .put("min_symbols", 1)
                    .put("min_numbers", 1);

    public CredentialMock() {
        this.schema = (new ObjectMapper()).createArrayNode();
        this.schema.add(value);
    }

    @Override
    public List<Credential> getMocks(MockarooApi api, int count) {
        List<Credential> credentials = new ArrayList<Credential>();
        ArrayNode jsonCredentials = api.genMock(this.getSchema(), count);
        for (JsonNode obj : jsonCredentials) {
            Credential credential = new Credential();
            credential.setType(Credential.PASSWORD);
            credential.setValue(obj.get("value").asText());
            credentials.add(credential);
        }
        return credentials;
    }
}
