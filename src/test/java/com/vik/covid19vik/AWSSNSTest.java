package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

public class AWSSNSTest {

    // ensure client is being instantiated with credentials
    @Test
    void testClientDefault() {
        SnsClient client = SnsClient.create();
        assertTrue(client.toString().contains("software.amazon.awssdk.services.sns.DefaultSnsClient@"));
        client.close();
    }

    // client builder
    @Test
    void testClientBuilder() {
        SnsClient client = SnsClient.create();
        assertTrue(client.toString().contains("software.amazon.awssdk.services.sns.DefaultSnsClient@"));
        String arn = "arn:aws:sns:us-west-2:404079974108:CovidLocatorSNS";
        final SubscribeRequest sr = SubscribeRequest.builder().topicArn(arn).protocol("sms").endpoint("4258923077").build();
        client.subscribe(sr);
        assertEquals("4258923077", sr.endpoint());
        assertEquals("sms", sr.protocol());
        assertEquals(arn, sr.topicArn());
        client.close();
    }
}
