package com.efub.bageasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication()
@EnableFeignClients
public class BageasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BageasyApplication.class, args);
	}

}
