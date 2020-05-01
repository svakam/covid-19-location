package com.vik.covid19vik;

import sun.awt.image.ImageWatched;

import java.util.HashSet;
import java.util.LinkedList;

public class CountriesGlobal {
    private String status;
    private LinkedList<String> dates;
    private LinkedList<Country> countries;

    // getters
    LinkedList<String> getDates() {
        return dates;
    }
    String getStatus() {
        return status;
    }
    LinkedList<Country> getCountries() {
        return countries;
    }
    // setters
    void setStatus(String status) {
        this.status = status;
    }
    void setDates(LinkedList<String> dates) {
        this.dates = dates;
    }
    void setCountries(LinkedList<Country> countries) {
        this.countries = countries;
    }

    static class Country {
        private String provinceOrState;
        private String countryOrRegion;
        private float lat;
        private float lon;
        private LinkedList<Integer> totalCases;
        private LinkedList<Integer> newCases;

        Country() {
            // empty arg constructor
        }

        // getters
        String getProvinceOrState() {
            return provinceOrState;
        }
        String getCountryOrRegion() {
            return countryOrRegion;
        }
        float getLat() {
            return lat;
        }
        float getLon() {
            return lon;
        }
        LinkedList<Integer> getTotalCases() {
            return totalCases;
        }
        LinkedList<Integer> getNewCases() {
            return newCases;
        }
        // setters
        void setProvinceOrState(String provinceOrState) {
            this.provinceOrState = provinceOrState;
        }
        void setCountryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
        }
        void setLat(float lat) {
            this.lat = lat;
        }
        void setLon(float lon) {
            this.lon = lon;
        }
        void setTotalCases(LinkedList<Integer> totalCases) {
            this.totalCases = totalCases;
        }
        void setNewCases(LinkedList<Integer> newCases) {
            this.newCases = newCases;
        }
    }

    static class ProvinceData {
        public LinkedList<Integer> newConfProvCases;
        public LinkedList<Integer> totalConfProvCases;
        public LinkedList<Integer> newDeathsProvCases;
        public LinkedList<Integer> totalDeathsProvCases;
        public LinkedList<Integer> newRecovProvCases;
        public LinkedList<Integer> totalRecovProvCases;

        // getters
        LinkedList<Integer> getNewConfProvCases() {
            return newConfProvCases;
        }
        LinkedList<Integer> getTotalConfProvCases() {
            return totalConfProvCases;
        }
        LinkedList<Integer> getNewDeathsProvCases() {
            return newDeathsProvCases;
        }
        LinkedList<Integer> getTotalDeathsProvCases() {
            return totalDeathsProvCases;
        }
        LinkedList<Integer> getNewRecovProvCases() {
            return newRecovProvCases;
        }
        LinkedList<Integer> getTotalRecovProvCases() {
            return totalRecovProvCases;
        }
        // setters
        void setNewConfProvCases(LinkedList<Integer> newConfProvCases) {
            this.newConfProvCases = newConfProvCases;
        }
        void setTotalConfProvCases(LinkedList<Integer> totalConfProvCases) {
            this.totalConfProvCases = totalConfProvCases;
        }
        void setNewDeathsProvCases(LinkedList<Integer> newDeathsProvCases) {
            this.newDeathsProvCases = newDeathsProvCases;
        }
        void setTotalDeathsProvCases(LinkedList<Integer> totalDeathsProvCases) {
            this.totalDeathsProvCases = totalDeathsProvCases;
        }
        void setNewRecovProvCases(LinkedList<Integer> newRecovProvCases) {
            this.newRecovProvCases = newRecovProvCases;
        }
        void setTotalRecovProvCases(LinkedList<Integer> totalRecovProvCases) {
            this.totalRecovProvCases = totalRecovProvCases;
        }
    }

    static LinkedList<Integer>[] retrieveCountryTSInfoAPICall(HashSet<String> countriesSeen, String searchedCountry, CountriesGlobal data) {
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForCountry = new LinkedList[4];

        LinkedList<CountriesGlobal.Country> countries = data.getCountries();
        int i = 0;
        while (true) {
            CountriesGlobal.Country country = countries.get(i);
            if (!countriesSeen.contains(country.getCountryOrRegion())) {

                // if a country has no provinces associated with it, it has the full time series data associated with it; just retrieve data normally
                if (country.getCountryOrRegion().equals(searchedCountry) && country.getProvinceOrState().equals("")) {
//                    System.out.println("match on country, province empty");
//                    System.out.println(country.countryOrRegion);
//                    System.out.println(country.provinceOrState);
                    LinkedList<Integer> newConfCases = country.getNewCases();
                    caseInfoForCountry[0] = newConfCases;
                    LinkedList<Integer> totalConfCases = country.getTotalCases();
                    caseInfoForCountry[1] = totalConfCases;

                    // get recent data
                    LinkedList<Integer> recentNewData = new LinkedList<>();
                    LinkedList<Integer> recentTotalData = new LinkedList<>();
                    recentNewData.add(newConfCases.get(newConfCases.size() - 1));
                    recentTotalData.add(totalConfCases.get(totalConfCases.size() - 1));
                    caseInfoForCountry[2] = recentNewData;
                    caseInfoForCountry[3] = recentTotalData;
//                    System.out.println(recentNewData);
//                    System.out.println(recentTotalData);

                    countriesSeen.add(country.getCountryOrRegion());
                    return caseInfoForCountry;
                }

                // if a country has provinces associated with it, add up each province data date by date to get country's total
                else if (country.getCountryOrRegion().equals(searchedCountry) && !country.getProvinceOrState().equals("")) {

                    // make sure there isn't an object associated with the country further downstream that doesn't have a province
                    // as long as the country is still the current country, do this check
                    LinkedList<Integer> allNewCases = new LinkedList<>();
                    LinkedList<Integer> allTotalCases = new LinkedList<>();
                    LinkedList<Integer> recentNewData = new LinkedList<>();
                    LinkedList<Integer> recentTotalData = new LinkedList<>();
                    while (i < countries.size()) {
//                        System.out.println("looping rest of countries " + countries.get(i).getCountryOrRegion());
                        if (countries.get(i).getCountryOrRegion().equals(searchedCountry) && countries.get(i).getProvinceOrState().equals("")) {
//                            System.out.println("country = " + countries.get(i).getCountryOrRegion() + ", province = " + countries.get(i).getProvinceOrState());
                            LinkedList<Integer> newConfCases = countries.get(i).getNewCases();
                            caseInfoForCountry[0] = newConfCases;
                            LinkedList<Integer> totalConfCases = countries.get(i).getTotalCases();
                            caseInfoForCountry[1] = totalConfCases;

                            // get recent data
                            recentNewData.add(newConfCases.get(newConfCases.size() - 1));
                            recentTotalData.add(totalConfCases.get(totalConfCases.size() - 1));
                            caseInfoForCountry[2] = recentNewData;
                            caseInfoForCountry[3] = recentTotalData;
//                            System.out.println(recentNewData);
//                            System.out.println(recentTotalData);

                            countriesSeen.add(country.getCountryOrRegion());
                            return caseInfoForCountry;

                        } else if (countries.get(i).getCountryOrRegion().equals(searchedCountry) && !countries.get(i).getProvinceOrState().equals("")) {
//                            System.out.println("country = " + countries.get(i).getCountryOrRegion() + ", province = " + countries.get(i).getProvinceOrState());
                            // if province exists, add case data to final array and go to next country
                            LinkedList<Integer> newConfCases = countries.get(i).getNewCases();
                            if (allNewCases.size() == 0) {
                                allNewCases.addAll(newConfCases);
                            }
                            // else iterate through total list and add current province's cases
                            else {
                                for (int caseIndex = 0; caseIndex < allNewCases.size(); caseIndex++) {
                                    allNewCases.set(caseIndex, (newConfCases.get(caseIndex) + allNewCases.get(caseIndex)));
                                }
                            }
                            LinkedList<Integer> totalConfCases = countries.get(i).getTotalCases();
                            if (allTotalCases.size() == 0) {
                                allTotalCases.addAll(totalConfCases);
                            } else {
                                // iterate through total list and add current province's cases
                                for (int caseIndex = 0; caseIndex < allTotalCases.size(); caseIndex++) {
                                    allTotalCases.set(caseIndex, (totalConfCases.get(caseIndex) + allTotalCases.get(caseIndex)));
                                }
                            }
                        }
                        i++;
                    }
                    caseInfoForCountry[0] = allNewCases;
                    caseInfoForCountry[1] = allTotalCases;
                    // add recent cases
                    recentNewData.add(allNewCases.get(allNewCases.size() - 1));
                    recentTotalData.add(allTotalCases.get(allTotalCases.size() - 1));
                    caseInfoForCountry[2] = recentNewData;
                    caseInfoForCountry[3] = recentTotalData;
//                    System.out.println(recentNewData);
//                    System.out.println(recentTotalData);
                    countriesSeen.add(country.getCountryOrRegion());
                    return caseInfoForCountry;
                }
            }
            i++;
        }
    }

    static LinkedList<Integer>[] retrieveProvinceTSInfoAPICall(String searchedProvince, CountriesGlobal data) {
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForProvince = new LinkedList[4];
        LinkedList<Country> countries = data.getCountries();
        int i = 0;
        while (!countries.get(i).getProvinceOrState().equals(searchedProvince)) {
            i++;
            if (i == countries.size()) {
                break;
            }
        }
        if (i == countries.size()) {
            System.out.println("Unable to pull province data for status: " + data.status);
            return null;
        } else {
            LinkedList<Integer> newCaseData = countries.get(i).newCases;
            LinkedList<Integer> totalCaseData = countries.get(i).totalCases;
            caseInfoForProvince[0] = newCaseData;
            caseInfoForProvince[1] = totalCaseData;

            // get recent data
            LinkedList<Integer> recentNewData = new LinkedList<>();
            LinkedList<Integer> recentTotalData = new LinkedList<>();
            recentNewData.add(newCaseData.get(newCaseData.size() - 1));
            recentTotalData.add(totalCaseData.get(totalCaseData.size() -1));
            caseInfoForProvince[2] = recentNewData;
            caseInfoForProvince[3] = recentTotalData;
            return caseInfoForProvince;
        }
    }
}
