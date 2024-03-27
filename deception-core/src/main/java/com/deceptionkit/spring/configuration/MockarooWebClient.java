package com.deceptionkit.spring.configuration;

import com.deceptionkit.mockaroo.exchange.interceptor.ApiKeyHeaderInterceptor;
import com.deceptionkit.spring.csv.DelimiterSeparatedValueHttpMessageConverter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
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
                .requestInterceptor(new ApiKeyHeaderInterceptor(apiKey))
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(DelimiterSeparatedValueHttpMessageConverter.csv(new CsvMapper())))
                .build();
    }
}
