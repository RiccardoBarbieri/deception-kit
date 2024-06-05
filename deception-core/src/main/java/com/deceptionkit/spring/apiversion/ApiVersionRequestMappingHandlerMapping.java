package com.deceptionkit.spring.apiversion;

import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ApiVersionRequestMappingHandlerMapping.class);

    private final String prefix;

    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if (info == null) return null;

        ApiVersion methodApiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        ApiVersion typeApiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);

        RequestMappingInfo newInfo = null;
        newInfo = createApiVersionInfo(methodApiVersion, typeApiVersion).combine(info);

        return newInfo;
    }

    private RequestMappingInfo createApiVersionInfo(ApiVersion methodApiVersion, ApiVersion typeApiVersion) {
        List<String> valuesList = new ArrayList<>();
        boolean typeOverrides = typeApiVersion != null && typeApiVersion.override();
        boolean methodOverrides = methodApiVersion != null && methodApiVersion.override();
        if (methodApiVersion != null) {
            if (!typeOverrides) {
                valuesList.addAll(List.of(methodApiVersion.value()));
            }
        }
        if (typeApiVersion != null) {
            if (!methodOverrides) {
                valuesList.addAll(List.of(typeApiVersion.value()));
            }
        }
        if (methodOverrides && typeOverrides) {
            logger.warn("Both method and type ApiVersion annotations are trying to override, keeping all values.");
            valuesList.addAll(List.of(methodApiVersion.value()));
            valuesList.addAll(List.of(typeApiVersion.value()));
        }

        //prepend prefix to all values and convert to array
        String[] patterns = valuesList.stream().map(value -> prefix + value).toArray(String[]::new);

        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(patterns);
        RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
        config.setPatternParser(new PathPatternParser());
        builder.options(config);

        return builder.build();

    }
}
