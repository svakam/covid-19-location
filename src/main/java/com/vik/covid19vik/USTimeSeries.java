package com.vik.covid19vik;

import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class USTimeSeries {
    private String status;
    private LinkedList<String> dates;
    private HashMap<String, State> statesWithCountyData;

    USTimeSeries() {
        // default empty-arg constructor
    }

    // getters and setters for USTimeSeries
    String getStatus() {
        return status;
    }
    void setStatus(String status) {
        this.status = status;
    }
    LinkedList<String> getDates() {
        return dates;
    }
    void setDates(LinkedList<String> dates) {
        this.dates = dates;
    }
    HashMap<String, State> getStatesWithCountyData() {
        return statesWithCountyData;
    }
    void setStatesWithCountyData(HashMap<String, State> statesWithCountyData) {
        this.statesWithCountyData = statesWithCountyData;
    }

    static class State {
        private String provinceOrState;
        private String countryOrRegion;
        HashMap<String, County> countyAndCases; // first linked list is total cases, second is new cases

        State() {
            // default empty-arg constructor
        }

        // getters and setters
        String getProvinceOrState() {
            return provinceOrState;
        }
        void setProvinceOrState(String provinceOrState) {
            this.provinceOrState = provinceOrState;
        }
        String getCountryOrRegion() {
            return countryOrRegion;
        }
        void setCountryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
        }
        HashMap<String, County> getCountyAndCases() {
            return countyAndCases;
        }
        void setCountyAndCases(HashMap<String, County> countyAndCases) {
            this.countyAndCases = countyAndCases;
        }

        static class County {
            private String county;
            private int population;
            private String combinedKey;
            private float lat;
            private float lon;
            private float uid;
            private String iso2;
            private String iso3;
            private int code3;
            private float fips;
            private LinkedList<Integer> newCases;
            private LinkedList<Integer> totalCases;

            County() {
                // default empty-arg constructor
            }

            String getCounty() {
                return county;
            }
            void setCounty(String county) {
                this.county = county;
            }
            int getPopulation() {
                return population;
            }
            void setPopulation(int population) {
                this.population = population;
            }
            String getCombinedKey() {
                return combinedKey;
            }
            void setCombinedKey(String combinedKey) {
                this.combinedKey = combinedKey;
            }
            float getLat() {
                return lat;
            }
            void setLat(float lat) {
                this.lat = lat;
            }
            float getLon() {
                return lon;
            }
            void setLon(float lon) {
                this.lon = lon;
            }
            protected float getUid() {
                return uid;
            }
            protected void setUid(float uid) {
                this.uid = uid;
            }

            String getIso2() {
                return iso2;
            }
            void setIso2(String iso2) {
                this.iso2 = iso2;
            }
            String getIso3() {
                return iso3;
            }
            void setIso3(String iso3) {
                this.iso3 = iso3;
            }
            int getCode3() {
                return code3;
            }
            void setCode3(int code3) {
                this.code3 = code3;
            }
            float getFips() {
                return fips;
            }
            void setFips(float fips) {
                this.fips = fips;
            }
            LinkedList<Integer> getNewCases() {
                return newCases;
            }
            void setNewCases(LinkedList<Integer> newCases) {
                this.newCases = newCases;
            }
            LinkedList<Integer> getTotalCases() {
                return totalCases;
            }
            void setTotalCases(LinkedList<Integer> totalCases) {
                this.totalCases = totalCases;
            }
        }
    }

    static LinkedList<Integer>[] retrieveProvinceTSInfoAPICall(String searchedProvince, USTimeSeries data) {
        @SuppressWarnings("unchecked") LinkedList<Integer>[] newTotalCasesAcrossCounties = new LinkedList[4];
        LinkedList<Integer> sumTotalCasesAcrossCounty = new LinkedList<>();
        LinkedList<Integer> sumNewCasesAcrossCounty = new LinkedList<>();

        // get each county's new and total cases
        HashMap<String, State> statesWithCountyData = data.getStatesWithCountyData();
        State state = statesWithCountyData.get(searchedProvince);
        if (state == null) {
            return null;
        }
        HashMap<String, State.County> allCounties = state.getCountyAndCases();
        for (Map.Entry<String, State.County> aCounty : allCounties.entrySet()) {
            State.County county = aCounty.getValue();
            LinkedList<Integer> newCases = county.getNewCases();
            LinkedList<Integer> totalCases = county.getTotalCases();

            // iterate new cases
            if (sumNewCasesAcrossCounty.size() == 0) {
                sumNewCasesAcrossCounty.addAll(newCases);
            } else {
                for (int caseIndex = 0; caseIndex < sumNewCasesAcrossCounty.size(); caseIndex++) {
                    sumNewCasesAcrossCounty.set(caseIndex, sumNewCasesAcrossCounty.get(caseIndex) + newCases.get(caseIndex));
                }
            }

            // iterate total cases
            if (sumTotalCasesAcrossCounty.size() == 0) {
                sumTotalCasesAcrossCounty.addAll(totalCases);
            } else {
                for (int caseIndex = 0; caseIndex < sumTotalCasesAcrossCounty.size(); caseIndex++) {
                    sumTotalCasesAcrossCounty.set(caseIndex, sumTotalCasesAcrossCounty.get(caseIndex) + totalCases.get(caseIndex));
                }
            }
        }

        newTotalCasesAcrossCounties[0] = sumNewCasesAcrossCounty;
        newTotalCasesAcrossCounties[1] = sumTotalCasesAcrossCounty;

        // get recent data
        LinkedList<Integer> recentNewData = new LinkedList<>();
        LinkedList<Integer> recentTotalData = new LinkedList<>();
        recentNewData.add(sumNewCasesAcrossCounty.get(sumNewCasesAcrossCounty.size() - 1));
        recentTotalData.add(sumTotalCasesAcrossCounty.get(sumTotalCasesAcrossCounty.size() -1));
        newTotalCasesAcrossCounties[2] = recentNewData;
        newTotalCasesAcrossCounties[3] = recentTotalData;
        return newTotalCasesAcrossCounties;
    }

    static class CountyCaseAndUIF extends CountryUIFLookup {
        // these names are misleading; used initially as a variable for totaled county data for a state's totals, but also for an individual county's data
        LinkedList<Integer> sumTotalCasesAcrossCounty;
        LinkedList<Integer> sumNewCasesAcrossCounty;
        int recentNewData;
        int recentTotalData;

        // getters and setters
        LinkedList<Integer> getSumTotalCasesAcrossCounty() {
            return sumTotalCasesAcrossCounty;
        }
        void setSumTotalCasesAcrossCounty(LinkedList<Integer> sumTotalCasesAcrossCounty) {
            this.sumTotalCasesAcrossCounty = sumTotalCasesAcrossCounty;
        }
        public LinkedList<Integer> getSumNewCasesAcrossCounty() {
            return sumNewCasesAcrossCounty;
        }
        public void setSumNewCasesAcrossCounty(LinkedList<Integer> sumNewCasesAcrossCounty) {
            this.sumNewCasesAcrossCounty = sumNewCasesAcrossCounty;
        }
        public int getRecentNewData() {
            return recentNewData;
        }
        public void setRecentNewData(int recentNewData) {
            this.recentNewData = recentNewData;
        }
        public int getRecentTotalData() {
            return recentTotalData;
        }
        public void setRecentTotalData(int recentTotalData) {
            this.recentTotalData = recentTotalData;
        }
    }

    static CountyCaseAndUIF retrieveCountyTSInfoAPICall(String searchedCounty, String searchedProvince, USTimeSeries data) {
        HashMap<String, State> statesWithCountyData = data.getStatesWithCountyData();
        CountyCaseAndUIF countyCaseAndUIF = new CountyCaseAndUIF();
        if (statesWithCountyData.get(searchedProvince) != null) {
            State stateWithCountyData = statesWithCountyData.get(searchedProvince);
            HashMap<String, State.County> countiesData = stateWithCountyData.getCountyAndCases();
            State.County countyData = countiesData.get(searchedCounty);
            countyCaseAndUIF.setSumNewCasesAcrossCounty(countyData.getNewCases());
            countyCaseAndUIF.setSumTotalCasesAcrossCounty(countyData.getTotalCases());
            countyCaseAndUIF.setRecentNewData(countyData.getNewCases().get(countyData.getNewCases().size() - 1));
            countyCaseAndUIF.setRecentTotalData(countyData.getTotalCases().get(countyData.getTotalCases().size() - 1));
            countyCaseAndUIF.setUid(countyData.getUid());
            countyCaseAndUIF.setIso2(countyData.getIso2());
            countyCaseAndUIF.setIso3(countyData.getIso3());
            countyCaseAndUIF.setCode3(countyData.getCode3());
            countyCaseAndUIF.setFips((int) countyData.getFips());
            countyCaseAndUIF.setCombinedKey(countyData.getCombinedKey());
            countyCaseAndUIF.setPopulation(countyData.getPopulation());
            System.out.println("Successfully pulled county data");
            return countyCaseAndUIF;
        }
        System.out.println("Could not pull county data");
        return null;
    }

    static LinkedList<String> createCountyDropdown(String searchedProvince, USTimeSeries data) {
        LinkedList<String> countyDropdown = new LinkedList<>();
        HashMap<String, State> statesWithCountyData = data.getStatesWithCountyData();
        if (statesWithCountyData.get(searchedProvince) != null) {
            State stateWithCountyData = statesWithCountyData.get(searchedProvince);
            HashMap<String, State.County> countiesData = stateWithCountyData.getCountyAndCases();
            for (Map.Entry<String, State.County> entry : countiesData.entrySet()) {
                if (!entry.getKey().equals("")) {
                    countyDropdown.add(entry.getKey());
                }
            }
            return countyDropdown;
        }
        System.out.println("Could not access statesWithCountyData");
        return null;
    }
}
