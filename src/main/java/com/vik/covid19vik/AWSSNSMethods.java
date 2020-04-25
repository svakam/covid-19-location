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
    static String[] subscribeEndpoint(String snsAdd, String currentURL) {
        String[] returnVals = new String[3];

        // instantiate SNS client
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

        // validate endpoint
        String regexComplete = "\\+1\\d{10}";
        String regexNoCountryCode = "\\d{10}";
        Pattern compiledComplete = Pattern.compile(regexComplete);
        Pattern compiledNoCountrycode = Pattern.compile(regexNoCountryCode);
        Matcher matcherComplete = compiledComplete.matcher(snsAdd);
        Matcher matcherNoCC = compiledNoCountrycode.matcher(snsAdd);

        String endpoint;

        // if valid number, subscribe and publish
        if (matcherComplete.matches()) {
            returnVals[2] = snsAdd;
            AWSSNSMethods.subscribeAndPublish(arn, snsAdd, client);
            endpoint = "valid";
        } else if (matcherNoCC.matches()) {
            String snsAddAndCC = "+1" + snsAdd;
            returnVals[2] = snsAddAndCC;
            AWSSNSMethods.subscribeAndPublish(arn, snsAddAndCC, client);
            endpoint = "valid";
        }
        // else don't subscribe/publish
        else {
            endpoint = "false";
        }

        // accounts for redundant requestparam
        currentURL = AWSSNSMethods.checkURLRedundant(currentURL);
        client.close();

        returnVals[0] = currentURL;
        returnVals[1] = endpoint;

        return returnVals;
    }

    // subscribe and publish
    static void subscribeAndPublish(String arn, String snsAdd, SnsClient client) {
        // subscribe request
        final SubscribeRequest sr = SubscribeRequest.builder()
                .topicArn(arn)
                .protocol("sms")
                .endpoint(snsAdd)
                .build();
        client.subscribe(sr);

        // publish request
        final String msg = "Thank you for subscribing to COVID-19 Locator. To unsubscribe, please reply STOP";
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
                .message(msg)
                .build();
        final PublishResponse publishResponse = client.publish(pr);
        System.out.println("Published response = " + publishResponse.messageId());
        client.close();
    }

    // check URL for redundant parameters
    static String checkURLRedundant(String currentURL) {
        if (currentURL.contains("&endpoint=true")) {
            int index = currentURL.indexOf("&endpoint=true");
            currentURL = currentURL.substring(0, index);
        }
        if (currentURL.contains("&endpoint=false")) {
            int index = currentURL.indexOf("&endpoint=false");
            currentURL = currentURL.substring(0, index);
        }
        return currentURL;
    }

}
