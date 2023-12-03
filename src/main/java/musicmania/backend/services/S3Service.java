package musicmania.backend.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {
    private final AmazonS3 s3client;

    @Autowired
    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadFile(String bucketName, MultipartFile file, String fileName) throws IOException{
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        return s3client.getUrl(bucketName, fileName).toString();
    }
}
