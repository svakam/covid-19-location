package com.vik.covid19vik;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.String;

import java.util.LinkedList;
import java.util.Queue;

public class TestAssocViaDelineation {
    static class Country {
        private String provinceOrState;
        private String countryOrRegion;
        private float lat;
        private float lon;
        private String status;
        private LinkedList<String> dates;
        private LinkedList<Integer> cases;

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
        public LinkedList<Integer> getCases() {
            return cases;
        }

        public void setProvinceOrState(String provinceOrState) {
            this.provinceOrState = provinceOrState;
        }

        public void setCountryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public void setLon(float lon) {
            this.lon = lon;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setDates(LinkedList<String> dates) {
            this.dates = dates;
        }

        public void setCases(LinkedList<Integer> cases) {
            this.cases = cases;
        }
    }

    @Test
    void testRead() {
        LinkedList<Country> countries = new LinkedList<>();

        // create a string of data points delineated by commas; first row are labels, second row and onward are data points
        java.lang.String test = "Province/State,Country/Region,Lat,Long,1/22/20,1/23/20\n,Afghanistan,33.0,65.0,0,0\n,Albania,41.1533,20.1683,0,0\nAustralian Capital Territory,Australia,-35.4735,149.0124,0,0";
        System.out.println(test);

        // create a class that stores province/state, country/region, lat, long, status, dates, cases

        // loop over test until a given string equals "Long"
        Queue<Character> labelMaker = new LinkedList<>();
        int i = 0;
        // labels delineated by a comma
        // when string equals "Long", skip that string and begin storing dates as strings
        long lengthOfCSV = test.length();
        System.out.println(lengthOfCSV);
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
                    break;
                }
            }

            // store dates as array of strings
            LinkedList<String> dates = new LinkedList<>();
            while (true) {
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
                    dates.add(newDate);
                    System.out.println(dates);
                    break;
                }
                dates.add(newDate);
                i++;
            }
            System.out.println(test.charAt(i));

            // instantiate data of each country and store in array
        do {
            Country country = new Country();
            // set status
            country.setStatus("confirmed");
            // set dates
            country.setDates(dates);

            // set province/state
            if (test.charAt(i) == '\n') {
                i++;
            }
            if (test.charAt(i) == ',') {
                country.setProvinceOrState("");
            } else {
                StringBuilder provinceState = new StringBuilder();
                while (test.charAt(i) != ',') {
                    provinceState.append(test.charAt(i));
                    i++;
                }
                country.setProvinceOrState(provinceState.toString());
                System.out.println(provinceState.toString());
            }
            i++;

            // set country/region
            StringBuilder countryRegion = new StringBuilder();
            while (test.charAt(i) != ',') {
                countryRegion.append(test.charAt(i));
                i++;
            }
            country.setCountryOrRegion(countryRegion.toString());
            System.out.println(countryRegion.toString());
            i++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            while (test.charAt(i) != ',') {
                lat.append(test.charAt(i));
                i++;
            }
            float l = Float.parseFloat(lat.toString());
            country.setLat(l);
            System.out.println(l);
            i++;
            StringBuilder lon = new StringBuilder();
            while (test.charAt(i) != ',') {
                lon.append(test.charAt(i));
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
                while (test.charAt(i) != ',') {
                    noOfCasesB.append(test.charAt(i));
                    i++;
                    if (i >= lengthOfCSV || test.charAt(i) == '\n') {
                        break;
                    }
                }
                String noOfCasesS = noOfCasesB.toString();
                int noOfCases = Integer.parseInt(noOfCasesS);
                confirmedSeries.add(noOfCases);
                if (i >= lengthOfCSV || test.charAt(i) == '\n') {
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
