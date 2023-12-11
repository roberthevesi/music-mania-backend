package musicmania.backend.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailService {

    Dotenv dotenv = Dotenv.load();

    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
            Objects.requireNonNull(dotenv.get("AWS_ACCESS_KEY_ID")),
            Objects.requireNonNull(dotenv.get("AWS_SECRET_ACCESS_KEY"))
    );
    private final AmazonSimpleEmailService sesClient;

    public EmailService() {
        this.sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.EU_WEST_3) // Use your appropriate AWS region
                .build();
    }

    public void sendEmail(String to, String subject, String body) {
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
                .withSource(dotenv.get("SES_VERIFIED_ACCOUNT")); // Use your verified email address

        sesClient.sendEmail(request);
    }
}
