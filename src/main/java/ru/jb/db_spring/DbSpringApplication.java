package ru.jb.db_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.jb.db_spring.service.impl.UserServiceImpl;

@SpringBootApplication
public class DbSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbSpringApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserServiceImpl userService) {
		return args -> {
			userService.createUserWithNotes();
		};
	}
}
