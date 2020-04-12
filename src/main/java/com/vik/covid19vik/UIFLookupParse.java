package com.vik.covid19vik;

import java.util.LinkedList;

public class UIFLookupParse {

    static void parseData() {
        LinkedList<CountryUIFLookup> countries = new LinkedList<>();

        String data = JHUPullMethods.getUIFLookup();

        System.out.println(data);
    }
}
