package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class AWSSNSTest {
    @Test
    void testClient() {
        SnsClient client = SnsClient.create();
        System.out.println(client);
    }
    
}
