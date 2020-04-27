package com.vik.covid19vik;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AWSDynamoDBMethods {
    // add requests to DB
    static void addRequestToDB(String snsAdd, String countryCheck, String provinceCheck, String countyCheck) {

        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        // each item is a collection of attributes: primary key always required, others are not
        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("endpoint", AttributeValue.builder().s(snsAdd).build());
        if (countryCheck != null) {
            itemValues.put("countries", AttributeValue.builder().s(countryCheck).build());
        }
        if (provinceCheck != null) {
            itemValues.put("provinces", AttributeValue.builder().s(provinceCheck).build());
        }
        if (countyCheck != null) {
            itemValues.put("counties", AttributeValue.builder().s(countyCheck).build());
        }

        PutItemRequest request = PutItemRequest.builder()
                .tableName("cv19location")
                .item(itemValues)
                .build();

        try {
            client.putItem(request);
            System.out.println("table updated");
        } catch (ResourceNotFoundException e) {
            System.out.print("Error: table can't be found");
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
        }

        client.close();
    }

    // could be useful if user wants to enter number to find out what they are subscribed to
//    static void getRequestToDB() {
//
//    }

    static void getRequestToDB(String snsAdd) {
        DynamoDbClient client = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("endpoint", AttributeValue.builder().s(snsAdd).build());

        try {
            GetItemRequest request = GetItemRequest.builder()
                    .key(keyToGet)
                    .tableName("cv19location")
                    .build();

            Map<String, AttributeValue> returnedItem = client.getItem(request).item();
            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                if (keys.size() == 0) {
                    System.out.println("No items found with that endpoint");
                }
                for (String key : keys) {
                    System.out.format("%s: %s\n", key, returnedItem.get(key).s());
                }
            } else {
                System.out.println("No item found with that key");
            }
        } catch(DynamoDbException e) {
            System.err.println(e.getMessage());
        }

        client.close();
    }

    // check URL for redundant parameters
    static String checkURLRedundant(String currentURL) {
        if (currentURL.contains("&checks=")) {
            int index = currentURL.indexOf("&checks=");
            currentURL = currentURL.substring(0, index);
        }
        return currentURL;
    }
}
