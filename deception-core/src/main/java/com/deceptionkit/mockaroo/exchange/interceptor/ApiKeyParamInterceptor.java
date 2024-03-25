package com.deceptionkit.mockaroo.exchange.interceptor;

import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class ApiKeyParamInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public ApiKeyParamInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getHeaders().containsKey("X-API-Key")) {
            return execution.execute(request, body);
        }
        HttpRequest newRequest =
        UriBuilder uriBuilder = UriComponentsBuilder.fromUri(request.getURI());
        uriBuilder.queryParam("key", apiKey);
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
    }
}
