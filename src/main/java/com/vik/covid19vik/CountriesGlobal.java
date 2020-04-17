package com.vik.covid19vik;

import java.util.HashSet;
import java.util.LinkedList;

public class CountriesGlobal {
    private String status;
    private LinkedList<String> dates;
    private LinkedList<Country> countries;

    // getters
    protected LinkedList<String> getDates() {
        return dates;
    }
    protected String getStatus() {
        return status;
    }
    protected LinkedList<Country> getCountries() {
        return countries;
    }
    // setters
    protected void setStatus(String status) {
        this.status = status;
    }
    protected void setDates(LinkedList<String> dates) {
        this.dates = dates;
    }
    protected void setCountries(LinkedList<Country> countries) {
        this.countries = countries;
    }

    static class Country {
        private String provinceOrState;
        private String countryOrRegion;
        private float lat;
        private float lon;
        private LinkedList<Integer> totalCases;
        private LinkedList<Integer> newCases;

        public Country() {
            // empty arg constructor
        }

        // getters
        protected String getProvinceOrState() {
            return provinceOrState;
        }
        protected String getCountryOrRegion() {
            return countryOrRegion;
        }
        protected float getLat() {
            return lat;
        }
        protected float getLon() {
            return lon;
        }
        protected LinkedList<Integer> getTotalCases() {
            return totalCases;
        }
        protected LinkedList<Integer> getNewCases() {
            return newCases;
        }
        // setters
        protected void setProvinceOrState(String provinceOrState) {
            this.provinceOrState = provinceOrState;
        }
        protected void setCountryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
        }
        protected void setLat(float lat) {
            this.lat = lat;
        }
        protected void setLon(float lon) {
            this.lon = lon;
        }
        protected void setTotalCases(LinkedList<Integer> totalCases) {
            this.totalCases = totalCases;
        }
        protected void setNewCases(LinkedList<Integer> newCases) {
            this.newCases = newCases;
        }
    }

    protected static class NewAndConf {
        private LinkedList<Integer> newProvCases;
        private LinkedList<Integer> totalProvCases;

        // getters
        protected LinkedList<Integer> getNewProvCases() {
            return newProvCases;
        }
        protected LinkedList<Integer> getTotalProvCases() {
            return totalProvCases;
        }
        // setters
        protected void setNewProvCases(LinkedList<Integer> newProvCases) {
            this.newProvCases = newProvCases;
        }
        protected void setTotalProvCases(LinkedList<Integer> totalProvCases) {
            this.totalProvCases = totalProvCases;
        }
    }

    protected static class ProvinceData {
        public LinkedList<Integer> newConfProvCases;
        public LinkedList<Integer> totalConfProvCases;
        public LinkedList<Integer> newDeathsProvCases;
        public LinkedList<Integer> totalDeathsProvCases;
        public LinkedList<Integer> newRecovProvCases;
        public LinkedList<Integer> totalRecovProvCases;

        // getters
        protected LinkedList<Integer> getNewConfProvCases() {
            return newConfProvCases;
        }
        protected LinkedList<Integer> getTotalConfProvCases() {
            return totalConfProvCases;
        }
        protected LinkedList<Integer> getNewDeathsProvCases() {
            return newDeathsProvCases;
        }
        protected LinkedList<Integer> getTotalDeathsProvCases() {
            return totalDeathsProvCases;
        }
        protected LinkedList<Integer> getNewRecovProvCases() {
            return newRecovProvCases;
        }
        protected LinkedList<Integer> getTotalRecovProvCases() {
            return totalRecovProvCases;
        }
        // setters
        protected void setNewConfProvCases(LinkedList<Integer> newConfProvCases) {
            this.newConfProvCases = newConfProvCases;
        }
        protected void setTotalConfProvCases(LinkedList<Integer> totalConfProvCases) {
            this.totalConfProvCases = totalConfProvCases;
        }
        protected void setNewDeathsProvCases(LinkedList<Integer> newDeathsProvCases) {
            this.newDeathsProvCases = newDeathsProvCases;
        }
        protected void setTotalDeathsProvCases(LinkedList<Integer> totalDeathsProvCases) {
            this.totalDeathsProvCases = totalDeathsProvCases;
        }
        protected void setNewRecovProvCases(LinkedList<Integer> newRecovProvCases) {
            this.newRecovProvCases = newRecovProvCases;
        }
        protected void setTotalRecovProvCases(LinkedList<Integer> totalRecovProvCases) {
            this.totalRecovProvCases = totalRecovProvCases;
        }
    }

    protected static LinkedList<Integer>[] retrieveCountryTSInfoAPICall(HashSet<String> countriesSeen, String searchedCountry, CountriesGlobal data) {
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForCountry = new LinkedList[2];

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
                    countriesSeen.add(country.getCountryOrRegion());
                    return caseInfoForCountry;
                }

                // if a country has provinces associated with it, add up each province data date by date to get country's total
                else if (country.getCountryOrRegion().equals(searchedCountry) && !country.getProvinceOrState().equals("")) {

                    // make sure there isn't an object associated with the country further downstream that doesn't have a province
                    // as long as the country is still the current country, do this check
                    LinkedList<Integer> allNewCases = new LinkedList<>();
                    LinkedList<Integer> allTotalCases = new LinkedList<>();
                    int j = i;
                    int restOfCountries = countries.size() - i;
                    while (j < restOfCountries) {
//                        System.out.println("looping rest of countries " + countries.get(j).getCountryOrRegion());
                        if (countries.get(j).getCountryOrRegion().equals(searchedCountry) && countries.get(j).getProvinceOrState().equals("")) {
                            System.out.println("country = " + countries.get(j).getCountryOrRegion() + ", province = " + countries.get(j).getProvinceOrState());
                            LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                            caseInfoForCountry[0] = newConfCases;
                            LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                            caseInfoForCountry[1] = totalConfCases;
                            countriesSeen.add(country.getCountryOrRegion());
                            return caseInfoForCountry;
                        } else if (countries.get(j).getCountryOrRegion().equals(searchedCountry) && !countries.get(j).getProvinceOrState().equals("")) {
//                            System.out.println("country = " + countries.get(j).getCountryOrRegion() + ", province = " + countries.get(j).getProvinceOrState());

                            // if province exists, add case data to final array and go to next country
                            LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                            if (allNewCases.size() == 0) {
                                allNewCases.addAll(newConfCases);
                            }
                            // else iterate through total list and add current province's cases
                            else {
                                for (int caseIndex = 0; caseIndex < allNewCases.size(); caseIndex++) {
                                    allNewCases.set(caseIndex, (newConfCases.get(caseIndex) + allNewCases.get(caseIndex)));
                                }
                            }
                            LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                            if (allTotalCases.size() == 0) {
                                allTotalCases.addAll(totalConfCases);
                            } else {
                                // iterate through total list and add current province's cases
                                for (int caseIndex = 0; caseIndex < allTotalCases.size(); caseIndex++) {
                                    allTotalCases.set(caseIndex, (totalConfCases.get(caseIndex) + allTotalCases.get(caseIndex)));
                                }
                            }
                        }
                        j++;
                    }
                    caseInfoForCountry[0] = allNewCases;
                    caseInfoForCountry[1] = allTotalCases;
                    countriesSeen.add(country.getCountryOrRegion());
                    return caseInfoForCountry;
                }
            }
            i++;
        }
    }

    protected static NewAndConf retrieveProvinceTSInfoAPICall(String searchedProvince, CountriesGlobal data) {
        NewAndConf caseInfoForCountry = new NewAndConf();
        LinkedList<CountriesGlobal.Country> countries = data.getCountries();
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
            caseInfoForCountry.setNewProvCases(countries.get(i).newCases);
            caseInfoForCountry.setTotalProvCases(countries.get(i).totalCases);
            return caseInfoForCountry;
        }
    }
}
