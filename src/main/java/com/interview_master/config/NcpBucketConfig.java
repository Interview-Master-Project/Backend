package com.interview_master.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpBucketConfig {

    @Value("${cloud.ncp.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.ncp.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.ncp.credentials.end-point}")
    private String endPoint;

    @Value("${cloud.ncp.region.static}")
    private String region;

    @Bean
    public AmazonS3Client objectStorageClient() {

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
