package com.deceptionkit.cli.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class IdProviderLogic {

    private final String baseUrl = "http://localhost:8015";

    private final String mockGenerationUrl = baseUrl + "/generateIdProviderResources";

    private final String clientsRegistrationUrl = baseUrl + "/registerClients";

    private final String groupsRegistrationUrl = baseUrl + "/registerGroups";

    private final String usersRegistrationUrl = baseUrl + "/registerUsers";

    private final String rolesRegistrationUrl = baseUrl + "/registerRoles";

    private final String roleAssignmentUrl = baseUrl + "/assignRoles";

    public IdProviderLogic() {

    }

    public JsonNode generateIdProviderResources(File definitionFile) {
//        try {
//            return new ObjectMapper().readTree("{\"users\":[{\"username\":\"user1\",\"enabled\":true,\"firstName\":\"User 1\",\"lastName\":\"Lastname 1\",\"email\":\"user1@localhost.com\",\"groups\":[\"group1\",\"group2\"],\"credentials\":[{\"type\":\"password\",\"value\":\"password\"}]},{\"username\":\"lshord\",\"enabled\":true,\"firstName\":\"Layney\",\"lastName\":\"Shord\",\"email\":\"layney.shord@localhost.com\",\"groups\":[\"group2\",\"Sales\"],\"credentials\":[{\"type\":\"password\",\"value\":\"smjnxZ0@8C\"}]},{\"username\":\"lbalasini\",\"enabled\":true,\"firstName\":\"Leigha\",\"lastName\":\"Balasini\",\"email\":\"leigha.balasini@localhost.com\",\"groups\":[\"group2\",\"Marketing\"],\"credentials\":[{\"type\":\"password\",\"value\":\"xjhjmF9/21wEn1~#\"}]},{\"username\":\"fkenworthey\",\"enabled\":true,\"firstName\":\"Francoise\",\"lastName\":\"Kenworthey\",\"email\":\"francoise.kenworthey@localhost.com\",\"groups\":[\"Sales\",\"Marketing\"],\"credentials\":[{\"type\":\"password\",\"value\":\"qivvrD6~.M%\"}]},{\"username\":\"rhaizelden\",\"enabled\":false,\"firstName\":\"Reinald\",\"lastName\":\"Haizelden\",\"email\":\"reinald.haizelden@localhost.com\",\"groups\":[\"Research and Development\",\"group2\"],\"credentials\":[{\"type\":\"password\",\"value\":\"dhgzqG7!oLJ'<Oq++E8\"}]},{\"username\":\"pgartrell\",\"enabled\":false,\"firstName\":\"Phyllis\",\"lastName\":\"Gartrell\",\"email\":\"phyllis.gartrell@localhost.com\",\"groups\":[\"Research and Development\",\"Sales\"],\"credentials\":[{\"type\":\"password\",\"value\":\"ldageT7&W|\\\\,G(\"}]},{\"username\":\"rbard\",\"enabled\":true,\"firstName\":\"Rhianon\",\"lastName\":\"Bard\",\"email\":\"rhianon.bard@localhost.com\",\"groups\":[\"Sales\",\"Marketing\"],\"credentials\":[{\"type\":\"password\",\"value\":\"mhpwrJ7.Dph8rdh1I\"}]},{\"username\":\"phanratty\",\"enabled\":false,\"firstName\":\"Perle\",\"lastName\":\"Hanratty\",\"email\":\"perle.hanratty@localhost.com\",\"groups\":[\"group2\",\"Research and Development\"],\"credentials\":[{\"type\":\"password\",\"value\":\"vimpyB9'Wj9\"}]},{\"username\":\"mpicford\",\"enabled\":true,\"firstName\":\"Melitta\",\"lastName\":\"Picford\",\"email\":\"melitta.picford@localhost.com\",\"groups\":[\"Research and Development\",\"group2\"],\"credentials\":[{\"type\":\"password\",\"value\":\"rzvbkG1'\\\">eIh@aup|KE\"}]},{\"username\":\"gbakewell\",\"enabled\":false,\"firstName\":\"Gregoire\",\"lastName\":\"Bakewell\",\"email\":\"gregoire.bakewell@localhost.com\",\"groups\":[\"Marketing\",\"Research and Development\"],\"credentials\":[{\"type\":\"password\",\"value\":\"zztrpC0\\\"eZ\"}]}],\"groups\":[{\"name\":\"group1\",\"roles\":[\"role2\"]},{\"name\":\"group2\",\"roles\":[\"role2\"]},{\"name\":\"Sales\",\"roles\":[\"role2\"]},{\"name\":\"Marketing\",\"roles\":[\"role3\"]},{\"name\":\"Research and Development\",\"roles\":[\"role3\"]}],\"roles\":[{\"name\":\"role1\",\"description\":\"Role for role1\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\",\"realmRole\":false},{\"name\":\"role2\",\"description\":\"Role for role2\",\"composite\":false,\"clientRole\":false,\"realmName\":\"master\",\"clientName\":null,\"realmRole\":true},{\"name\":\"role3\",\"description\":\"Role for role3\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\",\"realmRole\":false}],\"clients\":[{\"clientId\":\"client1\",\"name\":\"Client 1\",\"description\":\"Client 1\",\"rootUrl\":\"http://localhost:8080\",\"adminUrl\":\"http://localhost:8080\",\"baseUrl\":\"http://localhost:8080\",\"enabled\":true,\"clientAuthenticatorType\":\"client-secret\",\"redirectUris\":[\"http://localhost:8080\"],\"webOrigins\":[],\"standardFlowEnabled\":null,\"implicitFlowEnabled\":null,\"directAccessGrantsEnabled\":null,\"publicClient\":false,\"protocol\":\"openid-connect\",\"attributes\":{\"oidc.ciba.grant.enabled\":\"true\",\"backchannel.logout.session.required\":\"true\",\"oauth2.device.authorization.grant.enabled\":\"true\",\"display.on.consent.screen\":\"false\",\"backchannel.logout.revoke.offline.tokens\":\"false\"},\"roles\":[{\"name\":\"role1\",\"description\":\"Role for role1\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\",\"realmRole\":false},{\"name\":\"role3\",\"description\":\"Role for role3\",\"composite\":false,\"clientRole\":true,\"realmName\":null,\"clientName\":\"client1\",\"realmRole\":false}]}]}");
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(mockGenerationUrl);
            httpPost.addHeader("Content-Type", "application/yaml");

            FileEntity fileEntity = new FileEntity(definitionFile);

            httpPost.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            return new ObjectMapper().readTree(EntityUtils.toString(entity));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerClients(JsonNode clients) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(clientsRegistrationUrl);
            httpPost.addHeader("Content-Type", "application/json");

            httpPost.setEntity(new StringEntity(clients.toString()));

            HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode() == 201;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerGroups(JsonNode groups) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(groupsRegistrationUrl);
            httpPost.addHeader("Content-Type", "application/json");

            httpPost.setEntity(new StringEntity(groups.toString()));

            HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode() == 201;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerUsers(JsonNode users) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(usersRegistrationUrl);
            httpPost.addHeader("Content-Type", "application/json");

            httpPost.setEntity(new StringEntity(users.toString()));

            HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode() == 201;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean registerRoles(JsonNode roles) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(rolesRegistrationUrl);
            httpPost.addHeader("Content-Type", "application/json");

            httpPost.setEntity(new StringEntity(roles.toString()));

            HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode() == 201;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean assignRoles(JsonNode groupRoleMapping) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(roleAssignmentUrl);
            httpPost.addHeader("Content-Type", "application/json");

            httpPost.setEntity(new StringEntity(groupRoleMapping.toString()));

            HttpResponse response = httpClient.execute(httpPost);

            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode getRoleNodeByName(ArrayNode roles, String roleName) {
        for (JsonNode role : roles) {
            if (role.get("name").asText().equals(roleName)) {
                return (ObjectNode) role;
            }
        }
        return null;
    }
}
