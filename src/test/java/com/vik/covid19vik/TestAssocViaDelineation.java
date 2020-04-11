package com.vik.covid19vik;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.String;

import java.util.LinkedList;
import java.util.Queue;

public class TestAssocViaDelineation {
    static class Data {
        private String provinceOrState;
        private String countryOrRegion;
        private float lat;
        private float lon;
        private String status;
        private LinkedList<String> dates = new LinkedList<>();
        private int[] cases;

        public String getProvinceOrState() {
                return provinceOrState;
            }
            public String getCountryOrRegion() {
                return countryOrRegion;
            }
            public float getLat() {
                return lat;
            }
            public float getLon() {
                return lon;
            }
            public String getStatus() {
                return status;
            }
            public LinkedList<String> getDates() {
                return dates;
            }
            public int[] getCases() {
                return cases;
            }
        }

    @Test
    void testRead() {
        // create a string of data points delineated by commas; first row are labels, second row and onward are data points
        java.lang.String test = "Province/State,Country/Region,Lat,Long,1/22/20,1/23/20\n,Afghanistan,33.0,65.0,0,0\n,Albania,41.1533,20.1683,0,0\nAustralian Capital Territory,Australia,-35.4735,149.0124,0,0";
        System.out.println(test);

        // create a class that stores province/state, country/region, lat, long, status, dates, cases

        // loop over test until a given string equals "Long"
        Queue<Character> labelMaker = new LinkedList<>();
        Data data = new Data();
        int i = 0;
        while (true) {
            while (test.charAt(i) != ',') {
                labelMaker.add(test.charAt(i));
                i++;
            }
            StringBuilder label = new StringBuilder();
            while (labelMaker.peek() != null) {
                label.append(labelMaker.poll());
            }
            i++;
            if (label.toString().equals("Long")) {
                System.out.println(test.charAt(i));
                break;
            }
        }
        while (true) {
            System.out.println(test.charAt(i));
            while (test.charAt(i) != ',') {
                labelMaker.add(test.charAt(i));
                i++;
            }
            StringBuilder date = new StringBuilder();
            while (labelMaker.peek() != null) {
                date.append(labelMaker.poll());
            }
            java.lang.String newDate = date.toString();
            if (newDate.contains("\n")) {
                newDate = newDate.replace("\n", "");
                data.getDates().add(newDate);
                break;
            }
            data.getDates().add(newDate);
            i++;
        }
        System.out.println(data.getDates());

        // labels delineated by a comma
        // when string equals "Long", skip that string and begin storing dates as strings
        // for every row after, store as data points associated with labels
        // if a comma proceeds a \n, store "" as province/state, else store items linearly associated with labels


        // end result:
        // [
        //     {
        //          Province/State: "",
        //          Country/Region: "Afghanistan",
        //          Lat: 33.0,
        //          Long: 65.0,
        //          Status: "Confirmed",
        //          Dates: ["1/22/20","1/23/20"]
        //          Cases: [0,0]
        //     },
        //     {
        //          Province/State: "",
        //          Country/Region: "Albania",
        //          Lat: 41.1533,
        //          Long: 20.1683,
        //          Status: "Confirmed",
        //          Dates: ["1/22/20", "1/23/20"].
        //          Cases: [0,0]
        //     },
        //     {
        //          Province/State: "Australian Capital Territory",
        //          Country/Region: "Australia",
        //          Lat: -35.4735,
        //          Long: 149.0124,
        //          Status: "Confirmed",
        //          Dates: ["1/22/20", "1/23/20"],
        //          Cases: [0,0]
        //     }
        // ]
    }
}
