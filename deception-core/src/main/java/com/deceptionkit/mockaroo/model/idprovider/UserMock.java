package com.deceptionkit.mockaroo.model.idprovider;

import com.deceptionkit.mockaroo.MockarooApi;
import com.deceptionkit.mockaroo.model.BaseMock;
import com.deceptionkit.model.Credential;
import com.deceptionkit.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class UserMock extends BaseMock<User> {

    private final ObjectNode username =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "username")
                    .put("type", "Formula")
                    .put("value", "concat(lower(firstName[0]), lower(lastName))");
    private final ObjectNode enabled =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "enabled")
                    .put("type", "Boolean");
    private final ObjectNode firstName =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "firstName")
                    .put("type", "First Name");
    private final ObjectNode lastName =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "lastName")
                    .put("type", "Last Name");
    private final ObjectNode email =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "email")
                    .put("type", "Formula")
                    .put("value", "concat(lower(firstName), '.', lower(lastName), '@', domain)");
    private final ObjectNode domain =
            (new ObjectMapper()).createObjectNode()
                    .put("name", "domain")
                    .put("type", "Custom List");
    private final Integer credentialsPerUser;
    private List<Credential> credentials;


    public UserMock(int credentialsPerUser, String domain) {
        this.schema = (new ObjectMapper()).createArrayNode();
        this.schema.add(this.domain.set("values", (new ObjectMapper()).createArrayNode().add(domain)));
        this.schema.add(firstName);
        this.schema.add(lastName);
        this.schema.add(enabled);
        this.schema.add(email);
        this.schema.add(username);
        this.credentialsPerUser = credentialsPerUser;
    }

    @Override
    public List<User> getMocks(MockarooApi api, int count) {
        List<User> users = new ArrayList<>();
        ArrayNode jsonUsers = api.genMock(this.schema, count);
        ArrayNode jsonCredentials = api.genMock(new CredentialMock().getSchema(), count * credentialsPerUser);
        for (JsonNode obj : jsonUsers) {
            User user = new User();
            user.setFirstName(obj.get("firstName").asText());
            user.setLastName(obj.get("lastName").asText());
            user.setEmail(obj.get("email").asText());
            user.setUsername(obj.get("username").asText());
            user.setEnabled(obj.get("enabled").asBoolean());
            user.setCredentials(new ArrayList<>());
            for (int i = 0; i < credentialsPerUser; i++) {
                Credential credential = new Credential();
                credential.setType(Credential.PASSWORD);
                credential.setValue(jsonCredentials.get(i).get("value").asText());
                jsonCredentials.remove(i);
                user.getCredentials().add(credential);
            }
            users.add(user);
        }
        return users;
    }
}
