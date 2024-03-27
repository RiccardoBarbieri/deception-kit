package com.deceptionkit.spring.configuration;

import com.deceptionkit.mockaroo.exchange.interceptor.ApiKeyParamInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MockarooWebClient {

    @Bean
    public RestClient mockarooClient(@Value("${MOCKAROO_API_KEY}") String apiKey) {
        return RestClient.builder()
                .baseUrl("https://api.mockaroo.com")
                .requestInterceptor(new ApiKeyParamInterceptor(apiKey))
                .build();
    }
}
