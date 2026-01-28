package com.disougie;

import static com.disougie.app_user.AppUserRole.ADMIN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.disougie.app_user.AppUser;
import com.disougie.app_user.AppUserRepository;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class RealEstatePlatformApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
		System.setProperty("MONGO_URL", dotenv.get("MONGO_URL"));
		System.setProperty("MONGO_DB_NAME", dotenv.get("MONGO_DB_NAME"));
		System.setProperty("SECRET_KEY", dotenv.get("SECRET_KEY"));
		
		ApplicationContext context = SpringApplication
				.run(RealEstatePlatformApplication.class, args);
		
		AppUserRepository repository = context.getBean(AppUserRepository.class);
		AppUser admin = AppUser.builder()
				.name("admin")
				.email("admin@system.com")
				.password("admin")
				.role(ADMIN)
				.build();
		repository.save(admin);
	}

}
