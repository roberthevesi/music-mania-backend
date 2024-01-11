package musicmania.backend.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import musicmania.backend.services.SecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AWSConfig {
    @Autowired
    private final SecretsManagerService secretsManagerService;

    public AWSConfig(SecretsManagerService secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
    }

    @Bean
    public AmazonS3 amazonS3Client() throws JsonProcessingException {
        String secretJson = secretsManagerService.getSecretJson("MUSIC_MANIA_AWS_CREDENTIALS");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> secretData = objectMapper.readValue(secretJson, Map.class);

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                secretData.get("AWS_ACCESS_KEY_ID"),
                secretData.get("AWS_SECRET_ACCESS_KEY")
        );

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }
}
