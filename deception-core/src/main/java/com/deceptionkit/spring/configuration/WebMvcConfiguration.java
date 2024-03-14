package com.deceptionkit.spring.configuration;

import com.deceptionkit.spring.apiversion.ApiVersionRequestMappingHandlerMapping;
import com.deceptionkit.spring.yamlconverter.YamlHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
//@EnableWebMvc
class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Bean
    @Primary
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping("v");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlHttpMessageConverter<>());
    }

}