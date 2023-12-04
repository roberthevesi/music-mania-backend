package musicmania.backend;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.annotation.MultipartConfig;
import musicmania.backend.configs.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(RsaKeyProperties.class)
@MultipartConfig
//@ComponentScan(basePackages = "musicmania.backend.configs")
public class BackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		SpringApplication.run(BackendApplication.class, args);
	}

}
