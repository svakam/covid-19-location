package com.vik.covid19vik;

import java.util.HashMap;
import java.util.LinkedList;

public class USTimeSeriesParse {
    protected static String parseDataToJSON(String status, String data) {

        USTimeSeries all = new USTimeSeries();
        HashMap<USTimeSeries.County, LinkedList<LinkedList<Integer>>> countyAndCases = new HashMap<>();

        // set status
        all.setStatus(status);

        

    }
}
