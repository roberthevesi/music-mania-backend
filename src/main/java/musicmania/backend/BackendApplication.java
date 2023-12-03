package musicmania.backend;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import musicmania.backend.configs.RsaKeyProperties;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(RsaKeyProperties.class)
@MultipartConfig
//@ComponentScan(basePackages = "musicmania.backend.configs")
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
