package com.vik.covid19vik;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class USTimeSeriesParse {
    protected static String parseDataToJSON(String status, String data) {

        USTimeSeries USData = new USTimeSeries();
        HashMap<String, USTimeSeries.State> statesWithCountyData = new HashMap<>();

        // set status
        USData.setStatus(status);

        int cursor = 0;
        int lengthOfCSV = data.length();
        System.out.println(lengthOfCSV);

        if (status.equals("confirmed")) {
            // loop over data until a given string equals "Combined_Key"
            while (true) {
                StringBuilder labelB = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    labelB.append(data.charAt(cursor));
                    cursor++;
                    if (labelB.toString().equals("Combined_Key")) {
                        break;
                    }
                }
                cursor++;
                if (labelB.toString().equals("Combined_Key")) {
                    break;
                }
            }
        } else if (status.equals("deaths")) {
            // loop over data until a given string equals "Population"
            while (true) {
                StringBuilder labelB = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    labelB.append(data.charAt(cursor));
                    cursor++;
                    if (labelB.toString().equals("Population")) {
                        break;
                    }
                }
                cursor++;
                if (labelB.toString().equals("Population")) {
                    break;
                }
            }
        }

        System.out.println("after combined key = " + data.charAt(cursor));

        // store dates as list of strings and set dates
        LinkedList<String> dates = new LinkedList<>();
        while (true) {
            StringBuilder date = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                date.append(data.charAt(cursor));
                cursor++;
                if (date.toString().contains("\n")) {
                    break;
                }
            }
            String newDate = date.toString();
            if (newDate.contains("\n")) {
                newDate = newDate.replace("\n", "");
                dates.add(newDate);
                System.out.println(dates);
                break;
            }
            dates.add(newDate);
            cursor++;
        }
        // set dates
        USData.setDates(dates);

        // instantiate data of each state and store in hashmap
        do {
            // being added to list of states, later optimizing to an array
            USTimeSeries.State newState = new USTimeSeries.State();

            // each state will contain state name, country name and hashmap<county name, county object>
            HashMap<String, USTimeSeries.County> countyAndCases = new HashMap<>();

            // county instance to be stored in hashmap as a value of key county name
            USTimeSeries.County newCounty = new USTimeSeries.County();

            // set UID
            if (data.charAt(cursor) == ',') {
                newCounty.setUid(-1);
            } else {
                StringBuilder uid = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    uid.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setUid(Integer.parseInt(uid.toString()));
                System.out.println("UID = " + uid.toString());
            }
            cursor++;

            // set iso2
            if (data.charAt(cursor) == ',') {
                newCounty.setIso2("");
            } else {
                StringBuilder iso2 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    iso2.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setIso2(iso2.toString());
                System.out.println("ISO2 = " + iso2.toString());
            }
            cursor++;

            // set iso3
            if (data.charAt(cursor) == ',') {
                newCounty.setIso3("");
            } else {
                StringBuilder iso3 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    iso3.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setIso3(iso3.toString());
                System.out.println("ISO3 = " + iso3.toString());
            }
            cursor++;

            // set code3
            if (data.charAt(cursor) == ',') {
                newCounty.setCode3(-1);
                System.out.println("code3 = " + newCounty.getCode3());
            } else {
                StringBuilder code3 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    code3.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setCode3(Integer.parseInt(code3.toString()));
                System.out.println("code3 = " + code3.toString());
            }
            cursor++;

            // set fips
            if (data.charAt(cursor) == ',') {
                newCounty.setFips(-1);
                System.out.println("fips = empty");
            } else {
                StringBuilder fips = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    fips.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setFips(Float.parseFloat(fips.toString()));
                System.out.println("fips = " + fips.toString());
            }
            cursor++;

            // set county
            if (data.charAt(cursor) == ',') {
                newCounty.setCounty("");
                System.out.println("county = empty");
            } else {
                StringBuilder county = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    county.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setCounty(county.toString());
                System.out.println(county.toString());
            }
            cursor++;

            // set province/state
            if (data.charAt(cursor) == ',') {
                newState.setProvinceOrState("");
                System.out.println("province/state = empty");
            } else {
                StringBuilder provinceOrState = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        provinceOrState.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        provinceOrState.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                newState.setProvinceOrState(provinceOrState.toString());
                System.out.println("province/state = " + provinceOrState.toString());
            }
            cursor++;

            // set country/region
            if (data.charAt(cursor) == ',') {
                newState.setCountryOrRegion("");
                System.out.println("country/region = empty");
            } else {
                StringBuilder countryRegion = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        countryRegion.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        countryRegion.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                newState.setCountryOrRegion(countryRegion.toString());
                System.out.println("country/region = " + countryRegion.toString());
            }
            cursor++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            if (data.charAt(cursor) == ',') {
                newCounty.setLat(5555);
                System.out.println("lat = " + newCounty.getLat());
            } else {
                while (data.charAt(cursor) != ',') {
                    lat.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setLat(Float.parseFloat(lat.toString()));
                System.out.println("lat = " + Float.parseFloat(lat.toString()));
            }
            cursor++;
            StringBuilder lon = new StringBuilder();
            if (data.charAt(cursor) == ',') {
                newCounty.setLon(5555);
                System.out.println("lat = " + newCounty.getLon());
            } else {
                while (data.charAt(cursor) != ',') {
                    lon.append(data.charAt(cursor));
                    cursor++;
                }
                newCounty.setLon(Float.parseFloat(lon.toString()));
                System.out.println("lon = " + Float.parseFloat(lon.toString()));
            }
            cursor++;

            // set combined key
            if (data.charAt(cursor) == ',') {
                newCounty.setCombinedKey("");
                System.out.println("combined key = empty");
            } else {
                StringBuilder combinedKey = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        combinedKey.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        combinedKey.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                newCounty.setCombinedKey(combinedKey.toString());
                System.out.println("combined key = " + combinedKey.toString());
            }
            cursor++;

            // deaths data doesn't contain population; if deaths, skip this section
            if (status.equals("deaths")) {
                // if population available, set population
                if (data.charAt(cursor) == ',') {
                    newCounty.setPopulation(-1);
                    System.out.println("population = " + newCounty.getPopulation());
                } else {
                    StringBuilder population = new StringBuilder();
                    while (data.charAt(cursor) != ',') {
                        population.append(data.charAt(cursor));
                        cursor++;
                        if (cursor >= lengthOfCSV) {
                            break;
                        }
                    }
                    newCounty.setPopulation(Integer.parseInt(population.toString()));
                System.out.println("population = " + Integer.parseInt(population.toString()));
                }
                cursor++;
            }

            // set total case data
            LinkedList<Integer> timeSeriesTotalCases = new LinkedList<>();
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
                timeSeriesTotalCases.add(noOfCases);
                if (cursor == lengthOfCSV || data.charAt(cursor) == '\n') {
                    break;
                }
                cursor ++;
            }
            newCounty.setTotalCases(timeSeriesTotalCases);

            // adjust cursor
            if (data.charAt(cursor) == '\n') {
                cursor++;
            }
            if (cursor == lengthOfCSV) {
                break;
            }

            // set list of new cases to county
            LinkedList<Integer> timeSeriesNewCases = addNewCaseList(timeSeriesTotalCases);
            newCounty.setNewCases(timeSeriesNewCases);

            // if state already exists, add county as a new county to that state object's hashmap
            if (statesWithCountyData.containsKey(newState.getProvinceOrState())) {
                USTimeSeries.State existingState = statesWithCountyData.get(newState.getProvinceOrState());
                HashMap<String, USTimeSeries.County> existingCountyAndCases = existingState.getCountyAndCases();
                existingCountyAndCases.put(newCounty.getCounty(), newCounty);
                statesWithCountyData.put(newState.getProvinceOrState(), existingState);
            }
            // else add county info and add new state to hashmap of states
            else {
                countyAndCases.put(newCounty.getCounty(), newCounty);
                newState.setCountyAndCases(countyAndCases);
                statesWithCountyData.put(newState.getProvinceOrState(), newState);
            }

        } while (cursor < lengthOfCSV);

        USData.setStatesWithCountyData(statesWithCountyData);

        Gson gson = new Gson();
        String json = gson.toJson(USData);
        System.out.println(json);
        return json;
    }

    private static LinkedList<Integer> addNewCaseList(LinkedList<Integer> totalCases) {
        LinkedList<Integer> newCases = new LinkedList<>();

        // use total case data and iterate to add difference between next and current day's case data to new case list
        for (int i = 0; i < totalCases.size(); i++) {
            if (i == 0) {
                newCases.add(0);
            } else {
                Integer newCase = totalCases.get(i) - totalCases.get(i - 1);
                newCases.add(newCase);
            }
        }
        return newCases;
    }
}
