package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.*;

public class AWSDynamoDBTest {
    @Test
    void testClient() {
        DynamoDbClient client = DynamoDbClient.create();
        System.out.println(client);
        assertTrue(client.toString().contains("software.amazon.awssdk.services.dynamodb.DefaultDynamoDbClient@"));
    }

    @Test
    void testGetTable() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }
}
