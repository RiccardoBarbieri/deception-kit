package com.deceptionkit.spring.configuration;

import com.deceptionkit.spring.apiversion.ApiVersionRequestMappingHandlerMapping;
import com.deceptionkit.spring.errorhandler.RestResponseStatusExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@EnableWebMvc
class RequestMappingConfiguration implements WebMvcConfigurer {

    @Bean
    @Primary
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping("v");
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(0, new RestResponseStatusExceptionResolver());
    }
}