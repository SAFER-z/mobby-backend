package com.safer.safer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SaferApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaferApplication.class, args);
	}

}
