package com.deceptionkit.spring.configuration;

import com.deceptionkit.mockaroo.exchange.interceptor.ApiKeyHeaderInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
public class MockarooWebClient {

    @Bean
    public RestClient mockarooClient(@Value("${MOCKAROO_API_KEY}") String apiKey) {
        return RestClient.builder()
                .baseUrl("https://api.mockaroo.com")
                .requestInterceptor(new ApiKeyHeaderInterceptor(apiKey))
//                .messageConverters(httpMessageConverters -> httpMessageConverters.add(DelimiterSeparatedValueHttpMessageConverter.csv(new CsvMapper())))
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    String body = new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining(" "));
                    throw new RuntimeException(body);
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    String body = new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining(" "));
                    throw new RuntimeException(body);
                })
                .build();
    }
}
