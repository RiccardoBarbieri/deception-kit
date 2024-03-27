package com.deceptionkit.spring.configuration;

import com.deceptionkit.spring.csv.DelimiterSeparatedValueHttpMessageConverter;
import com.deceptionkit.spring.yaml.YamlHttpMessageConverter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class MessageConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlHttpMessageConverter<>());
        converters.add(DelimiterSeparatedValueHttpMessageConverter.csv(new CsvMapper()));
    }
}
