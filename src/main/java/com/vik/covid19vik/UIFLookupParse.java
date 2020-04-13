package com.vik.covid19vik;

import java.util.LinkedList;

public class UIFLookupParse {

    static void parseData() {

        // pull data
        String data = JHUPullMethods.getUIFLookup();
        System.out.println(data);


    }
}
