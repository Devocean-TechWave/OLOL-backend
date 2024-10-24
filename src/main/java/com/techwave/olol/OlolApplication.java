package com.techwave.olol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
@EnableJpaAuditing
public class OlolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlolApplication.class, args);
	}

}
