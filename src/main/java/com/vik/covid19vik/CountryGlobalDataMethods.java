package com.vik.covid19vik;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Queue;

public class CountryGlobalDataMethods {

    // use time series pull methods to get data (in controller)

    static void parseData(String status, String data) {

        // parse data into json array
        LinkedList<Country> countries = new LinkedList<>();

        // create a string of data points delineated by commas; first row are labels, second row and onward are data points
        System.out.println(data);

        // create a class that stores province/state, country/region, lat, long, status, dates, cases

        // loop over data until a given string equals "Long"
        Queue<Character> labelMaker = new LinkedList<>();
        int i = 0;
        // labels delineated by a comma
        // when string equals "Long", skip that string and begin storing dates as strings
        long lengthOfCSV = data.length();
        System.out.println(lengthOfCSV);
        while (true) {
            while (data.charAt(i) != ',') {
                labelMaker.add(data.charAt(i));
                i++;
            }
            StringBuilder label = new StringBuilder();
            while (labelMaker.peek() != null) {
                label.append(labelMaker.poll());
            }
            i++;
            if (label.toString().equals("Long")) {
                break;
            }
        }

        // store dates as array of strings
        LinkedList<String> dates = new LinkedList<>();
        while (true) {
            while (data.charAt(i) != ',') {
                labelMaker.add(data.charAt(i));
                i++;
            }
            StringBuilder date = new StringBuilder();
            while (labelMaker.peek() != null) {
                date.append(labelMaker.poll());
            }
            java.lang.String newDate = date.toString();
            if (newDate.contains("\n")) {
                newDate = newDate.replace("\n", "");
                dates.add(newDate);
                System.out.println(dates);
                break;
            }
            dates.add(newDate);
            i++;
        }
        System.out.println(data.charAt(i));

        // instantiate data of each country and store in array
        do {
            Country country = new Country();
            // set status
            country.setStatus(status);
            // set dates
            country.setDates(dates);

            // set province/state
            if (data.charAt(i) == '\n') {
                i++;
            }
            if (data.charAt(i) == ',') {
                country.setProvinceOrState("");
            } else {
                StringBuilder provinceState = new StringBuilder();
                while (data.charAt(i) != ',') {
                    provinceState.append(data.charAt(i));
                    i++;
                }
                if (provinceState.toString().equals("\"Bonaire")) {
                    provinceState.append(',');
                    i++;
                    while (data.charAt(i) != ',') {
                        provinceState.append(data.charAt(i));
                        i++;
                    }
                }
                country.setProvinceOrState(provinceState.toString());
                System.out.println(provinceState.toString());
            }
            i++;

            // set country/region
            StringBuilder countryRegion = new StringBuilder();
            while (data.charAt(i) != ',') {
                countryRegion.append(data.charAt(i));
                i++;
            }
            if (countryRegion.toString().equals("\"Korea")) {
                System.out.println(data.charAt(i));
                countryRegion.append(',');
                i++;
                while (data.charAt(i) != ',') {
                    countryRegion.append(data.charAt(i));
                    i++;
                }
            }
            country.setCountryOrRegion(countryRegion.toString());
            System.out.println(countryRegion.toString());
            i++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            while (data.charAt(i) != ',') {
                lat.append(data.charAt(i));
                i++;
            }
            float l = Float.parseFloat(lat.toString());
            country.setLat(l);
            System.out.println(l);
            i++;
            StringBuilder lon = new StringBuilder();
            while (data.charAt(i) != ',') {
                lon.append(data.charAt(i));
                i++;
            }
            l = Float.parseFloat(lon.toString());
            System.out.println(l);
            country.setLon(l);
            i++;

            // set case data
            LinkedList<Integer> confirmedSeries = new LinkedList<>();
            // loop until all data points added
            while (true) {
                StringBuilder noOfCasesB = new StringBuilder();
                while (data.charAt(i) != ',') {
                    noOfCasesB.append(data.charAt(i));
                    i++;
                    if (i >= lengthOfCSV || data.charAt(i) == '\n') {
                        break;
                    }
                }
                String noOfCasesS = noOfCasesB.toString();
                int noOfCases = Integer.parseInt(noOfCasesS);
                confirmedSeries.add(noOfCases);
                if (i >= lengthOfCSV || data.charAt(i) == '\n') {
                    break;
                }
                i++;
            }
            System.out.println(confirmedSeries);
            country.setCases(confirmedSeries);
            countries.add(country);
        } while (i < lengthOfCSV);
        for (Country country : countries) {
            Gson gson = new Gson();
            String json = gson.toJson(country);
            System.out.println(json);
        }
    }
}
