package com.deceptionkit.generation.idprovider.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DefaultsProvider {

    private static final Logger log = LoggerFactory.getLogger(DefaultsProvider.class);


    private static String DEFAULTS_FILE;

    @Value("${client.default.file}")
    public void setDEFAULTS_FILE(String DEFAULTS_FILE) {
        this.DEFAULTS_FILE = DEFAULTS_FILE;
    }


    public DefaultsProvider() {

    }

    public static <T> T getClientDefault(String key, Class<T> type) {
        log.debug("DEFAULTS_FILE: {}", DEFAULTS_FILE);
//        try (InputStream is = new FileInputStream(ResourceUtils.getFile("classpath:" + DEFAULTS_FILE))) {
        try (InputStream is = new FileInputStream(DEFAULTS_FILE)) {
            JsonNode node = (new ObjectMapper()).readTree(is);
            return (new ObjectMapper()).treeToValue(node.get(key), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
