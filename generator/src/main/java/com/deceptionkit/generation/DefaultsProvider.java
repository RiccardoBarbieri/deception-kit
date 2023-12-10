package com.deceptionkit.generation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultsProvider {


    private DefaultsProvider() {

    }

    public static <T> T getClientDefault(String key, Class<T> type) {
        try (InputStream is = new FileInputStream("./src/main/resources/client_default.json")) {
            JsonNode node = (new ObjectMapper()).readTree(is);
            return (new ObjectMapper()).treeToValue(node.get(key), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
