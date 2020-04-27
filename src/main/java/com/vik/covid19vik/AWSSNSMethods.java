package com.vik.covid19vik;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AWSSNSMethods {

    // includes start client, validate number
    static void subscribeEndpoint(String snsAdd, String countryCheck, String provinceCheck, String countyCheck) {

        // instantiate client
        SnsClient client = SnsClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

        // get topic
        ListTopicsResponse ltr = client.listTopics();
        List<Topic> topics = ltr.topics();
        String arn = null;
        for (Topic topic : topics) {
            if (topic.topicArn().contains("CovidLocatorSNS")) {
                arn = topic.topicArn();
            }
        }
        if (arn == null) {
            throw new NullPointerException("Could not find topic ARN");
        }

        AWSSNSMethods.subscribeAndPublish(arn, snsAdd, client, countryCheck, provinceCheck, countyCheck);
    }

    // subscribe and publish
    static void subscribeAndPublish(String arn, String snsAdd, SnsClient client, String countryCheck, String provinceCheck, String countyCheck) {
        // subscribe request
        final SubscribeRequest sr = SubscribeRequest.builder()
                .topicArn(arn)
                .protocol("sms")
                .endpoint(snsAdd)
                .build();
        client.subscribe(sr);

        // publish request:
        // formulate message
        StringBuilder msg = new StringBuilder("Thank you for subscribing to COVID-19 Locator. You have opted to receive notifications for: ");
        if (countryCheck != null) {
            msg.append(countryCheck);
        }
        if (countryCheck != null && countyCheck != null && provinceCheck != null){
            msg.append(", ").append(provinceCheck).append(", and ").append(countyCheck);
        } else if (countryCheck != null && countyCheck == null && provinceCheck != null) {
            msg.append(" and ").append(provinceCheck);
        } else if (countryCheck == null && provinceCheck != null && countyCheck != null) {
            msg.append(provinceCheck).append(" and ").append(countyCheck).append(" county");
        } else if (countryCheck == null && provinceCheck == null && countyCheck != null) {
            msg.append(countyCheck).append(" county");
        }
        msg.append(". To unsubscribe, please reply STOP");

        // get subscription id
        String subscriptionArn = null;
        ListSubscriptionsResponse lsr = client.listSubscriptions();
        List<Subscription> subscriptions = lsr.subscriptions();
        for (Subscription subscription : subscriptions) {
            if (subscription.endpoint().equals(snsAdd)) {
                subscriptionArn = subscription.subscriptionArn();
            }
        }
        if (subscriptionArn == null) {
            throw new NullPointerException("Could not retrieve subscription arn for this endpoint");
        }
        final PublishRequest pr = PublishRequest.builder()
                .phoneNumber(snsAdd)
                .message(msg.toString())
                .build();
        final PublishResponse publishResponse = client.publish(pr);
        System.out.println("Published response = " + publishResponse.messageId());
        client.close();
    }

    // check URL for redundant parameters
    static String checkURLRedundant(String currentURL) {
        if (currentURL.contains("&endpoint=valid")) {
            int index = currentURL.indexOf("&endpoint=valid");
            currentURL = currentURL.substring(0, index);
        }
        if (currentURL.contains("&endpoint=invalid")) {
            int index = currentURL.indexOf("&endpoint=invalid");
            currentURL = currentURL.substring(0, index);
        }
        return currentURL;
    }

}
