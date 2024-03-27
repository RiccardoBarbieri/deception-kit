package com.deceptionkit.mockaroo.exchange.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

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
        //modify request uri adding parameter
        UriBuilder uriBuilder = UriComponentsBuilder.fromUri(request.getURI());
        uriBuilder.queryParam("key", apiKey);
        HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
            @Override
            public URI getURI() {
                return uriBuilder.build();
            }
        };
        return execution.execute(modifiedRequest, body);
    }
}
