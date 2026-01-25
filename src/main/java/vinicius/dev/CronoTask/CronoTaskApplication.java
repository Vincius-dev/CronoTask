package vinicius.dev.CronoTask;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CronoTaskApplication {

	public static void main(String[] args) {
		// Carrega variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.ignoreIfMissing()
				.load();
		
		// Define as variáveis de ambiente para o Spring
		dotenv.entries().forEach(entry -> 
			System.setProperty(entry.getKey(), entry.getValue())
		);
		
		SpringApplication.run(CronoTaskApplication.class, args);
	}
}
