package com.gdg.hackathon;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HackathonApplication {
	public static void main(String[] args){
		new SpringApplicationBuilder(HackathonApplication.class)
				.properties("spring.config.location=classpath:/application.yml,classpath:/application-secret.yml")
				.run(args);
	}

}