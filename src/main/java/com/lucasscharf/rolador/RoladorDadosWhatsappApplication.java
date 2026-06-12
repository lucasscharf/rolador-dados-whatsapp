package com.lucasscharf.rolador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RoladorDadosWhatsappApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoladorDadosWhatsappApplication.class, args);
	}

}
