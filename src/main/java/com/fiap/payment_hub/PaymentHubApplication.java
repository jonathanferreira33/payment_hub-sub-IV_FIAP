package com.fiap.payment_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.fiap.payment_hub.infrastructure.config")
public class PaymentHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentHubApplication.class, args);
	}

}
