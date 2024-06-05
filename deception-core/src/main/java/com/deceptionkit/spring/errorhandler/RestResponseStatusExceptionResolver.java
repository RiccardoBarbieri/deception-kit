package com.deceptionkit.spring.errorhandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component
public class RestResponseStatusExceptionResolver implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(RestResponseStatusExceptionResolver.class);


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("Error occurred while processing request: {} at {}", request.getMethod(), request.getRequestURI());
        log.error("Error message: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new MappingJackson2JsonView());
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }
}
