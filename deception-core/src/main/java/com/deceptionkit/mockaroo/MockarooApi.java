package com.deceptionkit.mockaroo;

import com.deceptionkit.mockaroo.model.idprovider.UserMock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MockarooApi {

//    private static final String apiKey = System.getenv("MOCKAROO_API_KEY");
    private static final String apiKey = System.getenv("MOCKAROO_API_KEY");

    private final String url = "https://api.mockaroo.com/api/";

    public static void main(String[] args) {
        MockarooApi api = new MockarooApi();
        UserMock userMock = new UserMock(1, "company.com");

    }

    public ArrayNode genMock(ArrayNode schema, int count) {
        URL url = null;
        try {
            url = new URL(this.url + "generate.json?" + "&count=" + count + "&array=true");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .setHeader("Content-Type", "application/json")
                    .setHeader("X-Api-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(schema.toString()))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = null;
        try {
            root = (ArrayNode) mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

}
