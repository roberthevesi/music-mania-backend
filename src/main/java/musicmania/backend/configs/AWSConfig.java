package musicmania.backend.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class AWSConfig {
    @Bean
    public AmazonS3 amazonS3Client(){
        Dotenv dotenv = Dotenv.load();

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                Objects.requireNonNull(dotenv.get("AWS_ACCESS_KEY_ID")),
                Objects.requireNonNull(dotenv.get("AWS_SECRET_ACCESS_KEY"))
        );

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }
}
