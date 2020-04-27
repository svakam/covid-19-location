package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AWSDynamoDBTest {
    @Test
    void testClient() {
        DynamoDbClient client = DynamoDbClient.create();
        System.out.println(client);
        assertTrue(client.toString().contains("software.amazon.awssdk.services.dynamodb.DefaultDynamoDbClient@"));
        client.close();
    }

    @Test
    void testGetTable() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        ListTablesRequest request = ListTablesRequest.builder().build();
        ListTablesResponse response = client.listTables(request);

        System.out.println("table names = " + response.tableNames());
        System.out.println("has table names = " + response.hasTableNames());

        DescribeTableRequest dtr = DescribeTableRequest.builder()
                .tableName("cv19location")
                .build();

        TableDescription tableInfo = client.describeTable(dtr).table();
        System.out.println("table name = " + tableInfo.tableName());
        System.out.println("table arn = " + tableInfo.tableArn());
        System.out.println("table status = " + tableInfo.tableStatus());
        System.out.println("item count = " + tableInfo.itemCount());
        for (AttributeDefinition a : tableInfo.attributeDefinitions()) {
            System.out.format("%s (%s)", a.attributeName(), a.attributeType());
        }

        client.close();
    }

    @Test
    void testAddToTable() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        // each item is a collection of attributes: primary key is always required, others are not
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("endpoint", AttributeValue.builder().s("enter number here").build());
        itemValues.put("countries", AttributeValue.builder().s("US").build());
        itemValues.put("provinces", AttributeValue.builder().s("Washington").build());
        itemValues.put("counties", AttributeValue.builder().s("King").build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("cv19location")
                .item(itemValues)
                .build();

        try {
            client.putItem(request);
            System.out.println("cv19location updated");
        } catch (ResourceNotFoundException e) {
            System.out.print("Error: table can't be found");
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testGetFromTable() {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        // getting an item by its primary key and the desired value
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("endpoint", AttributeValue.builder().s("enter number here").build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName("cv19location")
                .build();

        try {
            Map<String, AttributeValue> returnedItem = client.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Table attributes: ");

                if (keys.size() == 0) {
                    System.out.println("No items found with that endpoint");
                } else {
                    for (String aKey : keys) {
                        System.out.println("key1 = " + aKey + ", returned item = " + returnedItem.get(aKey).s());
                    }
                }
            } else {
                System.out.println("No item found with that endpoint");
            }
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }
        client.close();
    }
}
