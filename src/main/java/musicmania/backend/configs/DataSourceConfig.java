package musicmania.backend.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import musicmania.backend.services.SecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Autowired
    private SecretsManagerService secretsManagerService;

    @Bean
    public DataSource dataSource() throws IOException {
        String secretName = "MUSIC_MANIA_AWS_RDS_CREDENTIALS";
        String secretJson = secretsManagerService.getSecretJson(secretName);

        // Use Jackson's ObjectMapper to parse the JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> credentials = objectMapper.readValue(secretJson, Map.class);

        // Extract credentials
        String url = credentials.get("url");
        String username = credentials.get("username");
        String password = credentials.get("password");

        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
