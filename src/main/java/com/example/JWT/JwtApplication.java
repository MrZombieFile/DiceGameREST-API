package com.example.JWT;

import com.example.JWT.model.domain.AppUser;
//import com.example.JWT.model.domain.Role;
import com.example.JWT.model.service.AppService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

//@SpringBootApplication
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = {"com.example.JWT.model.repository"})
@ComponentScan("com.example.JWT.service")
@ComponentScan("com.example.JWT.domain")
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
/*
	@Bean
	CommandLineRunner run(AppService appService){
		return args -> {
			appService.addRole(new Role(null, "ROLE_USER"));

			appService.addUser(new AppUser(null, "Alfred", "alfred@email.com", "1234", new Integer[10][2], 0, 0, new ArrayList<>()));

			appService.addRoleToAppUSer("ROLE_USER", "alfred@email.com");
		};
	}
*/
}
