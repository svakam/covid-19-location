package com.vik.covid19vik;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Queue;

class CountryGlobalDataParse {

    // use time series pull methods to get data (in controller)

    protected static String parseDataToJSON(String status, String data) {

        CountryGlobal countryWithDates = new CountryGlobal();
        LinkedList<CountryGlobal.Country> countries = new LinkedList<>();

        // set status
        countryWithDates.setStatus(status);

        // string of data points delineated by commas; first row are labels, second row and onward are data points
//        System.out.println(data);

        // loop over data until a given string equals "Long"
        Queue<Character> labelMaker = new LinkedList<>();
        int cursor = 0;
        // labels delineated by a comma
        // when string equals "Long", skip that string and begin storing dates as strings
        int lengthOfCSV = data.length();
        System.out.println("length of CSV = " + lengthOfCSV);
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

        // store dates as array of strings and set dates
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
            String newDate = date.toString();
            if (newDate.contains("\n")) {
                newDate = newDate.replace("\n", "");
                dates.add(newDate);
//                System.out.println(dates);
                break;
            }
            dates.add(newDate);
            cursor++;
        }
        // set dates
        countryWithDates.setDates(dates);

        // instantiate data of each country and store in array
        do {
            CountryGlobal.Country countryInfo = new CountryGlobal.Country();

            // adjust counter for new country, or if end of file break
            if (data.charAt(cursor) == '\n') {
                cursor++;
            }
            if (cursor == lengthOfCSV) {
                break;
            }

            // set province/state
            if (data.charAt(cursor) == ',') {
                countryInfo.setProvinceOrState("");
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
                countryInfo.setProvinceOrState(provinceState.toString());
//                System.out.println(provinceState.toString());
            }
            cursor++;

            // set country/region
            StringBuilder countryRegion = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                countryRegion.append(data.charAt(cursor));
                cursor++;
            }
            if (countryRegion.toString().equals("\"Korea")) {
//                System.out.println(data.charAt(cursor));
                countryRegion.append(',');
                cursor++;
                while (data.charAt(cursor) != ',') {
                    countryRegion.append(data.charAt(cursor));
                    cursor++;
                }
            }
            countryInfo.setCountryOrRegion(countryRegion.toString());
//            System.out.println(countryRegion.toString());
            cursor++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                lat.append(data.charAt(cursor));
                cursor++;
            }
            float l = Float.parseFloat(lat.toString());
            countryInfo.setLat(l);
//            System.out.println(l);
            cursor++;
            StringBuilder lon = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                lon.append(data.charAt(cursor));
                cursor++;
            }
            l = Float.parseFloat(lon.toString());
//            System.out.println(l);
            countryInfo.setLon(l);
            cursor++;

            // set case data
            LinkedList<Integer> timeSeriesCases = new LinkedList<>();
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
                timeSeriesCases.add(noOfCases);
                if (cursor == lengthOfCSV || data.charAt(cursor) == '\n') {
                    break;
                }
                cursor++;
            }
//            System.out.println(timeSeriesCases);
            countryInfo.setTotalCases(timeSeriesCases);

            // set empty list for new cases; being added later
            LinkedList<Integer> timeSeriesNewCases = new LinkedList<>();
            countryInfo.setNewCases(timeSeriesNewCases);

            // add to list of countries
            countries.add(countryInfo);
            countryWithDates.setCountries(countries);

        } while (cursor < lengthOfCSV);

        // add in new case data and parse to json
        CountryGlobal countryWithDatesNewCases = addNewCaseList(countryWithDates);
        Gson gson = new Gson();
        return gson.toJson(countryWithDatesNewCases);
    }

    protected static CountryGlobal fromJSON(String status, String data) {
        String json = parseDataToJSON(status, data);
        Gson gson = new Gson();
        return gson.fromJson(json, CountryGlobal.class);
    }

    private static CountryGlobal addNewCaseList(CountryGlobal withoutNewCases) {
        for (CountryGlobal.Country country : withoutNewCases.getCountries()) {

            // for every country, get total case data, iterate through each, and subtract the difference between the current day's cases and cases from previous day
            // to get new cases for current day
            // add this difference as a new item in the new cases list

            LinkedList<Integer> totalCases = country.getTotalCases();
            LinkedList<Integer> newCases = country.getNewCases();
            for (int i = 0; i < totalCases.size(); i++) {
                if (i == 0) {
                    newCases.add(0);
                } else {
                    Integer newCase = totalCases.get(i) - totalCases.get(i - 1);
                    newCases.add(newCase);
                }
            }
            country.setNewCases(newCases);
        }

        return withoutNewCases;
    }
}
