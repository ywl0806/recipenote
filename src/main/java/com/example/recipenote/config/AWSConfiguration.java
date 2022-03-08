package com.example.recipenote.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String awsAccessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String awsSecretAccessKey;

    @Value("${AWS_DEFAULT_REGION}")
    private String awsDefaultRegion;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
    }
    @Bean
    public AmazonS3Client s3Client() {
        BasicAWSCredentials creds = new BasicAWSCredentials(awsAccessKeyId,awsSecretAccessKey);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(awsDefaultRegion)
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
    }


}
