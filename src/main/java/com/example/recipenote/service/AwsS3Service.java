package com.example.recipenote.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AwsS3Service {

    @Autowired
    private final AmazonS3 amazonS3;

    @Value("${AWS_BUCKET}")
    private String awsBucket;

    @Value("${AWS_DEFAULT_REGION}")
    private String awsDefaultRegion;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadImage(MultipartFile multipartFile, String dir) {
        String fileName = createFileName(multipartFile);
        String key = "/uploads" + dir + "/" +fileName;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
           amazonS3.putObject(new PutObjectRequest(awsBucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "fail to upload");
        }

        return "https://" + awsBucket + ".s3-" + awsDefaultRegion + ".amazonaws.com/" + key;
    }

    public String createFileName(MultipartFile multipartFile) {
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        return UUID.randomUUID() + "." + extension;
    }
}
