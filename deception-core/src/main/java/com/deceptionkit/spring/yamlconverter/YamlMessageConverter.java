package com.deceptionkit.spring.yamlconverter;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class YamlMessageConverter extends AbstractJackson2HttpMessageConverter {

    public YamlMessageConverter() {
        super(new YAMLMapper(),
                new MediaType("application", "yaml", StandardCharsets.UTF_8),
                new MediaType("text", "yaml", StandardCharsets.UTF_8),
                new MediaType("application", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("text", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("application", "yml", StandardCharsets.UTF_8),
                new MediaType("text", "yml", StandardCharsets.UTF_8),
                new MediaType("application", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("text", "*+yaml", StandardCharsets.UTF_8)
        );
    }

//    protected YamlMessageConverter(ObjectMapper objectMapper) {
//        super(objectMapper);
//    }
//
//    protected YamlMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
//        super(objectMapper, supportedMediaType);
//    }
//
//    protected YamlMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
//        super(objectMapper, supportedMediaTypes);
//    }
}
