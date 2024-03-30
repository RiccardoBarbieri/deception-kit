package com.deceptionkit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


@SpringBootApplication
public class DeceptionkitApplication {

	public static void main(String[] args) {
		SpringApplication springApp = new SpringApplication(DeceptionkitApplication.class);
		springApp.run(args);
	}

}