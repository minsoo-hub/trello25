package com.trello25.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String AWS_ACCESS_KEY;
    @Value("${cloud.aws.credentials.secret-key}")
    private String AWS_SECRET_KEY;
    @Value("${cloud.aws.region.static}")
    private String AWS_REGION;

    @Bean
    public AmazonS3 amazonS3() {
        //accesskey, secretkey 자격증명 만듬
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        return AmazonS3ClientBuilder.standard()
            .withRegion(AWS_REGION)
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .build();
    }
}
