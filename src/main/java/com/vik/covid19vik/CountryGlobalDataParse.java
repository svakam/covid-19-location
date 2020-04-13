package com.vik.covid19vik;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Queue;

public class CountryGlobalDataParse {

    // use time series pull methods to get data (in controller)

    static void parseData(String status, String data) {

        LinkedList<CountryGlobal> countries = new LinkedList<>();

        // string of data points delineated by commas; first row are labels, second row and onward are data points
        System.out.println(data);

        // loop over data until a given string equals "Long"
        Queue<Character> labelMaker = new LinkedList<>();
        int cursor = 0;
        // labels delineated by a comma
        // when string equals "Long", skip that string and begin storing dates as strings
        int lengthOfCSV = data.length();
        System.out.println(lengthOfCSV);
        while (true) {
            while (data.charAt(cursor) != ',') {
                labelMaker.add(data.charAt(cursor));
                cursor++;
            }
            StringBuilder label = new StringBuilder();
            while (labelMaker.peek() != null) {
                label.append(labelMaker.poll());
            }
            cursor++;
            if (label.toString().equals("Long")) {
                break;
            }
        }

        // store dates as array of strings
        LinkedList<String> dates = new LinkedList<>();
        while (true) {
            while (data.charAt(cursor) != ',') {
                labelMaker.add(data.charAt(cursor));
                cursor++;
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
            cursor++;
        }

        // instantiate data of each country and store in array
        do {
            CountryGlobal country = new CountryGlobal();

            // adjust counter for new country, or if end of file break
            if (data.charAt(cursor) == '\n') {
                cursor++;
            }
            if (cursor == lengthOfCSV) {
                break;
            }

            // set status
            country.setStatus(status);

            // set dates
            country.setDates(dates);

            // set province/state
            if (data.charAt(cursor) == ',') {
                country.setProvinceOrState("");
            } else {
                StringBuilder provinceState = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    provinceState.append(data.charAt(cursor));
                    cursor++;
                }
                if (provinceState.toString().equals("\"Bonaire")) {
                    provinceState.append(',');
                    cursor++;
                    while (data.charAt(cursor) != ',') {
                        provinceState.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                country.setProvinceOrState(provinceState.toString());
                System.out.println(provinceState.toString());
            }
            cursor++;

            // set country/region
            StringBuilder countryRegion = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                countryRegion.append(data.charAt(cursor));
                cursor++;
            }
            if (countryRegion.toString().equals("\"Korea")) {
                System.out.println(data.charAt(cursor));
                countryRegion.append(',');
                cursor++;
                while (data.charAt(cursor) != ',') {
                    countryRegion.append(data.charAt(cursor));
                    cursor++;
                }
            }
            country.setCountryOrRegion(countryRegion.toString());
            System.out.println(countryRegion.toString());
            cursor++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                lat.append(data.charAt(cursor));
                cursor++;
            }
            float l = Float.parseFloat(lat.toString());
            country.setLat(l);
            System.out.println(l);
            cursor++;
            StringBuilder lon = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                lon.append(data.charAt(cursor));
                cursor++;
            }
            l = Float.parseFloat(lon.toString());
            System.out.println(l);
            country.setLon(l);
            cursor++;

            // set case data
            LinkedList<Integer> confirmedSeries = new LinkedList<>();
            // loop until all data points added
            while (true) {
                StringBuilder noOfCasesB = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    noOfCasesB.append(data.charAt(cursor));
                    cursor++;
                    if (cursor == lengthOfCSV || data.charAt(cursor) == '\n') {
                        break;
                    }
                }
                String noOfCasesS = noOfCasesB.toString();
                int noOfCases = Integer.parseInt(noOfCasesS);
                confirmedSeries.add(noOfCases);
                if (cursor == lengthOfCSV || data.charAt(cursor) == '\n') {
                    break;
                }
                cursor++;
            }
            System.out.println(confirmedSeries);

            // add to list of countries
            country.setCases(confirmedSeries);
            countries.add(country);

        } while (cursor < lengthOfCSV);

        // json countries
        Gson gson = new Gson();
        String json = gson.toJson(countries);
        System.out.println(json);
    }
}
