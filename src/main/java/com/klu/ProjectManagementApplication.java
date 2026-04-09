package com.klu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	public org.springframework.boot.CommandLineRunner checkDbConnection(javax.sql.DataSource dataSource) {
		return args -> {
			try {
				String actualUrl = dataSource.getConnection().getMetaData().getURL();
				System.out.println("\n=======================================================");
				System.out.println("🚀 SPRING BOOT IS CURRENTLY CONNECTED TO: ");
				System.out.println("👉 " + actualUrl);
				System.out.println("=======================================================\n");
			} catch (Exception e) {
				System.out.println("Failed to get DB URL: " + e.getMessage());
			}
		};
	}
}
