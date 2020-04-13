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

    static class CountryLookup {
        private int uid;
        private String iso2;
        private String iso3;
        private int code3;
        private String fips;
        private String county;
        private String provinceOrState;
        private String countryOrRegion;
        private float lat;
        private float lon;
        private String combinedKey;
        private int population;
        public CountryLookup() {
            // empty arg constructor
        }
        // getters
        public int getUid() {
            return uid;
        }
        public String getIso2() {
            return iso2;
        }
        public String getIso3() {
            return iso3;
        }
        public int getCode3() {
            return code3;
        }
        public String getFips() {
            return fips;
        }
        public String getCounty() {
            return county;
        }
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
        public String getCombinedKey() {
            return combinedKey;
        }
        public int getPopulation() {
            return population;
        }
        // setters
        public void setUid(int uid) {
            this.uid = uid;
        }
        public void setIso2(String iso2) {
            this.iso2 = iso2;
        }
        public void setIso3(String iso3) {
            this.iso3 = iso3;
        }
        public void setCode3(int code3) {
            this.code3 = code3;
        }
        public void setFips(String fips) {
            this.fips = fips;
        }
        public void setCounty(String county) {
            this.county = county;
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
        public void setCombinedKey(String combinedKey) {
            this.combinedKey = combinedKey;
        }
        public void setPopulation(int population) {
            this.population = population;
        }
    }

    @Test
    void testGlobalParse() {
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

    @Test
    void testLookupParse() {
        // pull data
        String data = "UID,iso2,iso3,code3,FIPS,Admin2,Province_State,Country_Region,Lat,Long_,Combined_Key,Population\n4,AF,AFG,4,,,,Afghanistan,33.93911,67.709953,Afghanistan,38928341\n8,AL,ALB,8,,,,Albania,41.1533,20.1683,Albania,2877800\n12,DZ,DZA,12,,,,Algeria,28.0339,1.6596,Algeria,43851043";

        LinkedList<CountryLookup> countries = new LinkedList<>();
        int i = 0;
        int lengthOfCSV = data.length();
        System.out.println(lengthOfCSV);

        // loop over data until a given string equals "Population"
        while (true) {
            StringBuilder labelB = new StringBuilder();
            while (data.charAt(i) != ',') {
                labelB.append(data.charAt(i));
                i++;
                if (labelB.toString().equals("Population\n")) {
                    break;
                }
            }
            if (labelB.toString().equals("Population\n")) {
                break;
            }
            i++;
        }

        // instantiate data of each country and store in array
        do {
            CountryLookup country = new CountryLookup();

            // set UID (always available)
            StringBuilder uid = new StringBuilder();
            while (data.charAt(i) != ',') {
                uid.append(data.charAt(i));
                i++;
            }
            country.setUid(Integer.parseInt(uid.toString()));
            System.out.println("UID = " + uid.toString());
            i++;

            // set iso2
            if (data.charAt(i) == ',') {
                country.setIso2("");
            } else {
                StringBuilder iso2 = new StringBuilder();
                while (data.charAt(i) != ',') {
                    iso2.append(data.charAt(i));
                    i++;
                }
                country.setIso2(iso2.toString());
                System.out.println("ISO2 = " + iso2.toString());
            }
            i++;

            // set iso3
            if (data.charAt(i) == ',') {
                country.setIso3("");
            } else {
                StringBuilder iso3 = new StringBuilder();
                while (data.charAt(i) != ',') {
                    iso3.append(data.charAt(i));
                    i++;
                }
                country.setIso3(iso3.toString());
                System.out.println("ISO3 = " + iso3.toString());
            }
            i++;

            // set code3
            if (data.charAt(i) == ',') {
                country.setCode3(-1);
                System.out.println("code3 = " + country.getCode3());
            } else {
                StringBuilder code3 = new StringBuilder();
                while (data.charAt(i) != ',') {
                    code3.append(data.charAt(i));
                    i++;
                }
                country.setCode3(Integer.parseInt(code3.toString()));
                System.out.println("code3 = " + code3.toString());
            }
            i++;

            // set FIPS
            if (data.charAt(i) == ',') {
                country.setFips("");
                System.out.println("fips = empty");
            } else {
                StringBuilder fips = new StringBuilder();
                while (data.charAt(i) != ',') {
                    fips.append(data.charAt(i));
                    i++;
                }
                country.setFips(fips.toString());
                System.out.println("fips = " + fips.toString());
            }
            i++;

            // set County
            if (data.charAt(i) == ',') {
                country.setCounty("");
                System.out.println("county = empty");
            } else {
                StringBuilder county = new StringBuilder();
                while (data.charAt(i) != ',') {
                    county.append(data.charAt(i));
                    i++;
                }
                country.setIso3(county.toString());
                System.out.println(county.toString());
            }
            i++;

            // set province/state
            if (data.charAt(i) == ',') {
                country.setProvinceOrState("");
                System.out.println("province/state = empty");
            } else {
                StringBuilder provinceOrState = new StringBuilder();
                if (data.charAt(i) == '\"') {
                    i++;
                    provinceOrState.append(data.charAt(i));
                    while (data.charAt(i) != '\"') {
                        provinceOrState.append(data.charAt(i));
                        i++;
                    }
                } else {
                    while (data.charAt(i) != ',') {
                        provinceOrState.append(data.charAt(i));
                        i++;
                    }
                }
                country.setIso3(provinceOrState.toString());
                System.out.println("province/state = " + provinceOrState.toString());
            }
            i++;

            // set country/region
            if (data.charAt(i) == ',') {
                country.setCountryOrRegion("");
                System.out.println("country/region = empty");
            } else {
                StringBuilder provinceOrState = new StringBuilder();
                if (data.charAt(i) == '\"') {
                    i++;
                    provinceOrState.append(data.charAt(i));
                    while (data.charAt(i) != '\"') {
                        provinceOrState.append(data.charAt(i));
                        i++;
                    }
                } else {
                    while (data.charAt(i) != ',') {
                        provinceOrState.append(data.charAt(i));
                        i++;
                    }
                }
                country.setCountryOrRegion(provinceOrState.toString());
                System.out.println("country/region = " + provinceOrState.toString());
            }
            i++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            if (data.charAt(i) == ',') {
                country.setLat(5555);
                System.out.println("lat = " + country.getLat());
            } else {
                while (data.charAt(i) != ',') {
                    lat.append(data.charAt(i));
                    i++;
                }
                country.setLat(Float.parseFloat(lat.toString()));
                System.out.println("lat = " + Float.parseFloat(lat.toString()));
            }
            i++;
            StringBuilder lon = new StringBuilder();
            if (data.charAt(i) == ',') {
                country.setLon(5555);
                System.out.println("lat = " + country.getLon());
            } else {
                while (data.charAt(i) != ',') {
                    lon.append(data.charAt(i));
                    i++;
                }
                country.setLon(Float.parseFloat(lon.toString()));
                System.out.println("lat = " + Float.parseFloat(lon.toString()));
            }
            i++;

            // set combined key
            if (data.charAt(i) == ',') {
                country.setCombinedKey("");
                System.out.println("combined key = empty");
            } else {
                StringBuilder combinedKey = new StringBuilder();
                if (data.charAt(i) == '\"') {
                    i++;
                    combinedKey.append(data.charAt(i));
                    while (data.charAt(i) != '\"') {
                        combinedKey.append(data.charAt(i));
                        i++;
                    }
                } else {
                    while (data.charAt(i) != ',') {
                        combinedKey.append(data.charAt(i));
                        i++;
                    }
                }
                country.setCountryOrRegion(combinedKey.toString());
                System.out.println("combined key = " + combinedKey.toString());
            }
            i++;

            // set population
            if (data.charAt(i) == '\n') {
                country.setPopulation(0);
                System.out.println("population = " + country.getPopulation());
            } else {
                StringBuilder population = new StringBuilder();
                while (data.charAt(i) != '\n') {
                    population.append(data.charAt(i));
                    i++;
                    if (i >= lengthOfCSV) {
                        break;
                    }
                }
                country.setPopulation(Integer.parseInt(population.toString()));
                System.out.println("population = " + Integer.parseInt(population.toString()));
            }
            i++;
            countries.add(country);
        } while (i < lengthOfCSV);
        for (CountryLookup country : countries) {
            Gson gson = new Gson();
            String json = gson.toJson(country);
            System.out.println(json);
        }
    }
}
