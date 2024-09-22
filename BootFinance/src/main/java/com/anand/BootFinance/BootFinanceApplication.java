package com.anand.BootFinance;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.info.Info;
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title="BootFinance",
				description="REST API for a secure Banking Application",
				version = "v1.0",
				contact = @Contact(
						name="Anand Raj",
						email="anandraj28127@gmail.com",
						url = "https://github.com/andaz03/BootFinance"
				)
		),
		externalDocs = @ExternalDocumentation
				(
						description = "BootFinance",
						url = "https://github.com/andaz03/"
				)
)
public class BootFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootFinanceApplication.class, args);
	}

}
