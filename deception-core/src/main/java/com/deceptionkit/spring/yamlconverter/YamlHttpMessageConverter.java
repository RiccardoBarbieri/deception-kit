package com.deceptionkit.spring.yamlconverter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.OutputStreamWriter;

@Component
public class YamlHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    public YamlHttpMessageConverter() {
        super(
                new MediaType("text", "yaml"),
                new MediaType("text", "yml"),
                new MediaType("application", "yaml"),
                new MediaType("application", "yml")
        );
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Constructor baseConstructor = new Constructor(clazz, new LoaderOptions());
        Yaml parser = new Yaml(baseConstructor);
        return parser.loadAs(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotReadableException {
        Yaml parser = new Yaml();
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody());
        parser.dump(t, writer);
        writer.close();
    }
}
