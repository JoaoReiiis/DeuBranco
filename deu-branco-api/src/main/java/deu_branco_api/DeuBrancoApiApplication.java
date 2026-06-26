package deu_branco_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeuBrancoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeuBrancoApiApplication.class, args);
	}

}
