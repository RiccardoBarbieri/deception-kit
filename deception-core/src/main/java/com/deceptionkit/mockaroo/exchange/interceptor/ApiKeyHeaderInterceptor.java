package com.deceptionkit.mockaroo.exchange.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ApiKeyHeaderInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public ApiKeyHeaderInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().getQuery().contains("key")) {
            return execution.execute(request, body);
        }
        request.getHeaders().add("X-API-Key", apiKey);
        return execution.execute(request, body);
    }
}
