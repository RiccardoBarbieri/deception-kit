package com.deceptionkit.spring.yaml;

import com.deceptionkit.spring.exception.YamlFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.IOException;
import java.io.OutputStreamWriter;

@Component
public class YamlHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {


    private final Logger logger = LoggerFactory.getLogger(YamlHttpMessageConverter.class);

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
        T t = null;
        t = parser.loadAs(inputMessage.getBody(), clazz);
        return t;
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotReadableException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setWidth(120);
        options.setIndent(2);
        Yaml parser = new Yaml(options);

        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody());

        String str = parser.dumpAs(t, Tag.MAP, null);
        writer.write(str);

        writer.close();
    }
}
