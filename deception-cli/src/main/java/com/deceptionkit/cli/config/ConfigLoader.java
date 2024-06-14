package com.deceptionkit.cli.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private static ConfigLoader INSTANCE;

    private ObjectNode config;

    private ConfigLoader(String configPath) {
    }

    public static ConfigLoader initInstance(String configPath) throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new ConfigLoader(configPath);
        }
        INSTANCE.config = (ObjectNode) new ObjectMapper().readTree((new File(configPath)));
        return INSTANCE;
    }

    public static ConfigLoader getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("ConfigLoader not initialized");
        }
        return INSTANCE;
    }

    public String getBaseUrl() {
        return config.get("baseUrl").asText();
    }


}
