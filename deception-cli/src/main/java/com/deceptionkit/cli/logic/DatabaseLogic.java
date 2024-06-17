package com.deceptionkit.cli.logic;

import com.deceptionkit.cli.config.ConfigLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;

public class DatabaseLogic {

//    private final String baseUrl = ConfigLoader.getInstance().getBaseUrl();
    private final String baseUrl = "http://localhost:8015";

    private final String databaseGenerationUrl = baseUrl + "/generation/database/databases";

    private final String tableGenerationUrl = baseUrl + "/generation/database/tables";

    private final String userGenerationUrl = baseUrl + "/generation/database/users";

    private final String dockerfileGenerationUrl = baseUrl + "/generation/database/dockerfile";

    public DatabaseLogic() {
    }

    public JsonNode generateDatabases(File definitionFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(databaseGenerationUrl);
            httpPost.addHeader("Content-Type", "application/yaml");

            FileEntity fileEntity = new FileEntity(definitionFile);

            httpPost.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPost);

            return new ObjectMapper().readTree(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode generateTables(File definitionFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(tableGenerationUrl);
            httpPost.addHeader("Content-Type", "application/yaml");

            FileEntity fileEntity = new FileEntity(definitionFile);

            httpPost.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPost);

            return new ObjectMapper().readTree(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode generateUsers(File definitionFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(userGenerationUrl);
            httpPost.addHeader("Content-Type", "application/yaml");

            FileEntity fileEntity = new FileEntity(definitionFile);

            httpPost.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPost);

            return new ObjectMapper().readTree(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateDockerfile(File definitionFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(dockerfileGenerationUrl);
            httpPost.addHeader("Content-Type", "application/yaml");

            FileEntity fileEntity = new FileEntity(definitionFile);

            httpPost.setEntity(fileEntity);

            HttpResponse response = httpClient.execute(httpPost);

            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
