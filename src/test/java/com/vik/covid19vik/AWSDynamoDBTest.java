package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class AWSDynamoDBTest {
    @Test
    void testClient() {
        DynamoDbClient client = DynamoDbClient.create();
        System.out.println(client);
    }
}
