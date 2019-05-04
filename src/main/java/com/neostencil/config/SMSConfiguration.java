package com.neostencil.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSConfiguration {

  @Value("${aws.sns.accessKey}")
  private String accessKey;
  @Value("${aws.sns.secretKey}")
  private String secretKey;
  @Value("${aws.sns.region}")
  private String region;

  private static final Logger LOGGER = LoggerFactory.getLogger(SMSConfiguration.class);

  @Bean
  public AmazonSNS ssnClient() {
    // Create Amazon SNS Client
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion(region)
        .build();
    return snsClient;
  }

}