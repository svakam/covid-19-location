package com.vik.covid19vik;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.List;

public class AWSSNSMethods {
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
        if (currentURL.contains("&valid=true")) {
            int index = currentURL.indexOf("&valid=true");
            currentURL = currentURL.substring(0, index);
        }
        if (currentURL.contains("&valid=false")) {
            int index = currentURL.indexOf("&valid=false");
            currentURL = currentURL.substring(0, index);
        }
        return currentURL;
    }
}
