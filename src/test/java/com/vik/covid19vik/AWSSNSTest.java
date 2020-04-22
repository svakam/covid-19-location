package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;

public class AWSSNSTest {

    // ensure client is being instantiated with credentials
    @Test
    void testClientDefault() {
        SnsClient client = SnsClient.create();
        assertTrue(client.toString().contains("software.amazon.awssdk.services.sns.DefaultSnsClient@"));
        client.close();
    }

    @Test
    void testCredentials() {
        Region region = Region.US_WEST_2;
        SnsClient client = SnsClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        System.out.println(client);
    }

    @Test
    void testGetTopics() {
        SnsClient client = SnsClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        System.out.println(client);
        ListTopicsResponse ltr = client.listTopics();
        List<Topic> topics = ltr.topics();
        for (Topic topic : topics) {
            System.out.println(topic);
            System.out.println(topic.topicArn());
        }
    }

    // client builder
    @Test
    void testClientBuilder() {
        SnsClient client = SnsClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        // subscribe request
        String arn = "arn:aws:sns:us-west-2:404079974108:CovidLocatorSNS";
        final SubscribeRequest sr = SubscribeRequest.builder()
                .topicArn(arn)
                .protocol("sms")
                .endpoint("4252096602")
                .build();
        client.subscribe(sr);
        assertEquals("4252096602", sr.endpoint());
        assertEquals("sms", sr.protocol());
        assertEquals(arn, sr.topicArn());

        // run function to get latest update on information
        // need combined key, latest date and all case data for latest date
        final String msg = "For King County, Washington, USA on 04/21/20:\nNew confirmed cases: 100\nTotal confirmed cases: 4000\nNew fatalities: 50\nTotal fatalities: 100\nNew recoveries: N/A\nTotal recoveries: N/A";
        final PublishRequest pr = PublishRequest.builder()
                .topicArn(arn)
                .message(msg)
                .build();
        final PublishResponse publishResponse = client.publish(pr);
        System.out.println("Published response = " + publishResponse.messageId());

        client.close();
    }

    @Test
    void testDeleteEndpoint() {
        SnsClient client = SnsClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        String endpointToDelete = "+4258923077";
        String unsubArn = null;

        ListSubscriptionsResponse lsr = client.listSubscriptions();
        List<Subscription> subscriptions = lsr.subscriptions();
        for (Subscription sub : subscriptions) {
            System.out.println(sub);
            if (sub.endpoint().equals(endpointToDelete)) {
                unsubArn = sub.subscriptionArn();
            }
        }

        UnsubscribeRequest usr = UnsubscribeRequest.builder().subscriptionArn(unsubArn).build();
        client.unsubscribe(usr);

        client.close();
    }
}
