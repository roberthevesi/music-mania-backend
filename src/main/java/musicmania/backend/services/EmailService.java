package musicmania.backend.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private final SecretsManagerService secretsManagerService;
    private final AmazonSimpleEmailService sesClient;

    @Autowired
    public EmailService(SecretsManagerService secretsManagerService) throws JsonProcessingException {
        this.secretsManagerService = secretsManagerService;

        String secretJson = secretsManagerService.getSecretJson("MUSIC_MANIA_AWS_CREDENTIALS");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> secretData = objectMapper.readValue(secretJson, Map.class);

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                secretData.get("AWS_ACCESS_KEY_ID"),
                secretData.get("AWS_SECRET_ACCESS_KEY")
        );

        this.sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }

    public void sendEmail(String to, String subject, String body) throws JsonProcessingException {
        String secretJson = secretsManagerService.getSecretJson("SES_VERIFIED_ACCOUNT");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> secretData = objectMapper.readValue(secretJson, Map.class);

        String sender = secretData.get("data");

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to)
                )
                .withMessage(
                        new Message()
                                .withBody(
                                        new Body().withHtml(new Content().withCharset("UTF-8").withData(body))
                                )
                                .withSubject(
                                        new Content().withCharset("UTF-8").withData(subject)
                                )
                )
                .withSource(sender); // Use your verified email address

        sesClient.sendEmail(request);
    }
}
