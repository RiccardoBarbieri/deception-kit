package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class DemoApplication {


    public static void main(String[] args) {
//        Properties props = new Properties();
//        props.setProperty("spring.profile.activeProfile", "demo");
        SpringApplication.run(DemoApplication.class, args);
    }

}
