package com.vik.covid19vik;

import java.util.HashSet;
import java.util.LinkedList;

public class CountriesGlobal {
    private String status;
    private LinkedList<String> dates;
    private LinkedList<Country> countries;

    // getters
    public LinkedList<String> getDates() {
        return dates;
    }
    public String getStatus() {
        return status;
    }
    public LinkedList<Country> getCountries() {
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

    protected static LinkedList<Integer>[] retrieveTSCaseInfoAPICall(HashSet<String> countriesSeen, String sc, CountriesGlobal data) {
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForCountry = new LinkedList[2];

        LinkedList<CountriesGlobal.Country> countries = data.getCountries();
        int i = 0;
        while (true) {
            CountriesGlobal.Country country = countries.get(i);
            if (!countriesSeen.contains(country.getCountryOrRegion())) {

                // if a country has no provinces associated with it, it has the full time series data associated with it; just retrieve data normally
                if (country.getCountryOrRegion().equals(sc) && country.getProvinceOrState().equals("")) {
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
                else if (country.getCountryOrRegion().equals(sc) && !country.getProvinceOrState().equals("")) {

                    // make sure there isn't an object associated with the country further downstream that doesn't have a province
                    // as long as the country is still the current country, do this check
                    LinkedList<Integer> allNewCases = new LinkedList<>();
                    LinkedList<Integer> allTotalCases = new LinkedList<>();
                    int j = i;
                    int restOfCountries = countries.size() - i;
                    while (j < restOfCountries) {
//                        System.out.println("looping rest of countries " + countries.get(j).getCountryOrRegion());
                        if (countries.get(j).getCountryOrRegion().equals(sc) && countries.get(j).getProvinceOrState().equals("")) {
                            System.out.println("country = " + countries.get(j).getCountryOrRegion() + ", province = " + countries.get(j).getProvinceOrState());
                            LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                            caseInfoForCountry[0] = newConfCases;
                            LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                            caseInfoForCountry[1] = totalConfCases;
                            countriesSeen.add(country.getCountryOrRegion());
                            return caseInfoForCountry;
                        } else if (countries.get(j).getCountryOrRegion().equals(sc) && !countries.get(j).getProvinceOrState().equals("")) {
//                            System.out.println("country = " + countries.get(j).getCountryOrRegion() + ", province = " + countries.get(j).getProvinceOrState());

                            // if province exists, add case data to final array and go to next country
                            LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                            if (allNewCases.size() == 0) {
                                allNewCases.addAll(newConfCases);
                            }
                            // else iterate through total list and add current province's cases
                            else {
                                for (int caseIndex = 0; caseIndex < allNewCases.size(); caseIndex++) {
                                    // get cases at index of total cases, add province cases to those cases, and update at index of total cases
                                    allNewCases.set(caseIndex, (newConfCases.get(caseIndex) + allNewCases.get(caseIndex)));
                                }
                            }
                            LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                            if (allTotalCases.size() == 0) {
                                allTotalCases.addAll(totalConfCases);
                            } else {
                                // iterate through total list and add current province's cases
                                for (int caseIndex = 0; caseIndex < allTotalCases.size(); caseIndex++) {
                                    // get cases at index of total cases, add province cases to those cases, and update at index of total cases
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
}
