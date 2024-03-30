package com.deceptionkit.generation.idprovider.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DefaultsProvider {

    @Value("${client.default.file}")
    private static String DEFAULTS_FILE;


    private DefaultsProvider() {

    }

    public static <T> T getClientDefault(String key, Class<T> type) {
        try (InputStream is = new FileInputStream(DEFAULTS_FILE)) {
            JsonNode node = (new ObjectMapper()).readTree(is);
            return (new ObjectMapper()).treeToValue(node.get(key), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
