package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MockarooApi {

    private static final String apiKey = System.getenv("MOCKAROO_API_KEY");

    private final String apiUrl = "https://api.mockaroo.com/api/";

    private final String aiUrl = "https://www.mockaroo.com/rest/ai/generate_fields";

    private final String schemaUrl = "https://www.mockaroo.com/rest/schemas/download?preview=true";

    public ArrayNode genMockJson(ArrayNode schema, int count) {
        URL url = RequestUtils.getUrl(this.apiUrl + "generate.json?" + "&count=" + count + "&array=true");

        HttpRequest request = RequestUtils.createRequest(url, schema, apiKey);

        HttpResponse<String> response = RequestUtils.sendRequest(request);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = null;

        try {
            root = (ArrayNode) mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

    public List<String> genMockSql(ArrayNode schema, int count, String tableName) {
        URL url = RequestUtils.getUrl(this.apiUrl + "generate.sql?" + "&count=" + count + "&table_name=" + tableName);

        HttpRequest request = RequestUtils.createRequest(url, schema, apiKey);

        HttpResponse<String> response = RequestUtils.sendRequest(request);

        String responseBody = response.body();

        String[] lines = responseBody.split("\n");

        List<String> linesWithTableName = Arrays.stream(lines).map(line -> line.replace("insert into", "insert into " + tableName)).toList();


        return linesWithTableName;
    }

    //returns arraynode containing mockaroo definition
    //of a generated data stracture
    public ArrayNode generateTableTypes(String prompt, int fieldsCount) {
        URL url = RequestUtils.getUrl(this.aiUrl);

        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        requestBody.put("topic", prompt);
        requestBody.put("count", fieldsCount);
        requestBody.put("data", "");

        HttpRequest request = RequestUtils.createRequest(url, requestBody);

        HttpResponse<String> response = RequestUtils.sendRequest(request);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = null;
        try {
            root = (ArrayNode) mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for (JsonNode node : root) {
            ObjectNode objectNode = (ObjectNode) node;

            ObjectNode dataTypeNode = (ObjectNode) objectNode.get("data_type");
            String dataType = dataTypeNode.get("label").asText();
            Integer typeId = dataTypeNode.get("id").asInt();

            objectNode.put("type", dataType);
            objectNode.put("type_id", typeId);
            objectNode.remove("data_type");

            //remove null fields
            //and reform values into an array
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();

                if (fieldName.equals("values") && !objectNode.get(fieldName).isNull()) {
                    List<String> values = Arrays.stream(objectNode.get(fieldName).toString().replace("\"", "").split(", ")).toList();
                    ArrayNode valuesNode = new ObjectMapper().valueToTree(values);
                    objectNode.set(fieldName, valuesNode);
                }

                if (objectNode.get(fieldName).isNull()) {
                    fieldNames.remove();
                }
            }
        }

        return root;
    }

    public String getTableDefinition(ObjectNode schemaNode) throws RuntimeException {
        URL url = RequestUtils.getUrl(this.schemaUrl);

        HttpRequest request = RequestUtils.createRequest(url, schemaNode);
        HttpResponse<String> response = RequestUtils.sendRequest(request);

        String responseBody = response.body();

        int createEnd = responseBody.indexOf(");");
        if (createEnd == -1) {
            throw new RuntimeException("Could not find end of create statement");
        }
        return responseBody.substring(0, createEnd + 2);
    }

}
