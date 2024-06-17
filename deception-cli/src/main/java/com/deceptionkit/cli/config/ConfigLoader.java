package com.deceptionkit.cli.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private static ConfigLoader INSTANCE;

    private ObjectNode config;

    private String configPath;

    private ConfigLoader() {
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath() {
        return this.configPath;
    }

    public ObjectNode getConfig() {
        return this.config;
    }

    public void setConfig(ObjectNode config) {
        this.config = config;
    }

    public static ConfigLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigLoader();
        }
        return INSTANCE;
    }

    public String getBaseUrl() {
        try {
            this.setConfig((ObjectNode) new ObjectMapper().readTree((new File(configPath))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.getConfig().get("baseUrl").asText();
    }


}
