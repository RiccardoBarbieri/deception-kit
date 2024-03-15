package com.deceptionkit.spring.apiversion;

import com.deceptionkit.generation.GenerationController;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;

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

//        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
//        ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
//
//        RequestCondition<?> methodCondition = super.getCustomMethodCondition(method);
//        RequestCondition<?> typeCondition = super.getCustomTypeCondition(handlerType);
//
//        RequestMappingInfo newInfo = null;
//
//        if (methodAnnotation != null) {
//            logger.debug("methodAnnotation not null, follows methodCondition");
//            newInfo = createApiVersionInfo(null, methodAnnotation, methodCondition).combine(info);
//        } else {
//            if (typeAnnotation != null) {
//                newInfo = createApiVersionInfo(typeAnnotation, null, typeCondition).combine(info);
//            }
//        }

        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        RequestMappingInfo newInfo = null;
        if (methodAnnotation != null) {
            logger.debug("methodAnnotation not null, follows methodCondition");
            RequestCondition<?> methodCondition = super.getCustomMethodCondition(method);
            newInfo = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
        } else {
            ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
            if (typeAnnotation != null) {
                RequestCondition<?> typeCondition = super.getCustomTypeCondition(handlerType);
                newInfo = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
            }
        }

        return newInfo;
    }

//    private RequestMappingInfo createApiVersionInfo(ApiVersion typeAnnotation, ApiVersion methodAnnotation, RequestCondition<?> customCondition) {
//        String[] values = typeAnnotation.value();
//        String[] patterns = new String[values.length];
//        for (int i = 0; i < values.length; i++) {
//            patterns[i] = prefix + values[i];
//        }
//
//        logger.debug("customCondition: " + customCondition.toString());
//
//
//        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(patterns);
//        RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
//        config.setPatternParser(new PathPatternParser());
//        builder.options(config);
//
//        return builder.customCondition(customCondition).build();
//
//    }

    private RequestMappingInfo createApiVersionInfo(ApiVersion annotation, RequestCondition<?> customCondition) {
        String[] values = annotation.value();
        String[] patterns = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            patterns[i] = prefix + values[i];
        }

        logger.debug("customCondition: " + customCondition);

        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(patterns);
        RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
        config.setPatternParser(new PathPatternParser());
        builder.options(config);

        return builder.customCondition(customCondition).build();

    }
}
