package com.vik.covid19vik;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;

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
        final PublishRequest pr = PublishRequest.builder()
                .topicArn(arn)
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
