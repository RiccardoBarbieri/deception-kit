package com.deceptionkit.mockaroo.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestUtils {

    public static HttpResponse<String> sendRequest(HttpRequest request) throws RuntimeException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;

    }

    public static URL getUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    public static HttpRequest createRequest(URL url, Object requestBody) throws RuntimeException {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .setHeader("Content-Type", "application/json")
//                    .setHeader("X-Api-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return request;
    }

    public static HttpRequest createRequest(URL url, Object requestBody, String apiKey) throws RuntimeException {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(url.toURI())
                    .setHeader("Content-Type", "application/json")
                    .setHeader("X-Api-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return request;
    }
}
