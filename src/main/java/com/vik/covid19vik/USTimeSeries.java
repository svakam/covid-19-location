package com.vik.covid19vik;

import java.util.HashMap;
import java.util.LinkedList;

class USTimeSeries {
    private String status;
    private LinkedList<String> dates;
    private HashMap<String, State> statesWithCountyData;

    protected static class State {
        private String provinceOrState;
        private String countryOrRegion;
        HashMap<String, County> countyAndCases; // first linked list is total cases, second is new cases

        // getters and setters
        protected String getProvinceOrState() {
            return provinceOrState;
        }
        public void setProvinceOrState(String provinceOrState) {
            this.provinceOrState = provinceOrState;
        }
        public String getCountryOrRegion() {
            return countryOrRegion;
        }
        public void setCountryOrRegion(String countryOrRegion) {
            this.countryOrRegion = countryOrRegion;
        }
        public HashMap<String, County> getCountyAndCases() {
            return countyAndCases;
        }
        public void setCountyAndCases(HashMap<String, County> countyAndCases) {
            this.countyAndCases = countyAndCases;
        }
    }

    protected static class County {
        private String county;
        private int population;
        private String combinedKey;
        private float lat;
        private float lon;
        private int uid;
        private String iso2;
        private String iso3;
        private int code3;
        private float fips;
        private LinkedList<Integer> newCases;
        private LinkedList<Integer> totalCases;

        public String getCounty() {
            return county;
        }
        public void setCounty(String county) {
            this.county = county;
        }
        public int getPopulation() {
            return population;
        }
        public void setPopulation(int population) {
            this.population = population;
        }
        public String getCombinedKey() {
            return combinedKey;
        }
        public void setCombinedKey(String combinedKey) {
            this.combinedKey = combinedKey;
        }
        public float getLat() {
            return lat;
        }
        public void setLat(float lat) {
            this.lat = lat;
        }
        public float getLon() {
            return lon;
        }
        public void setLon(float lon) {
            this.lon = lon;
        }
        protected int getUid() {
            return uid;
        }
        protected void setUid(int uid) {
            this.uid = uid;
        }

        public String getIso2() {
            return iso2;
        }
        public void setIso2(String iso2) {
            this.iso2 = iso2;
        }
        public String getIso3() {
            return iso3;
        }
        public void setIso3(String iso3) {
            this.iso3 = iso3;
        }
        public int getCode3() {
            return code3;
        }
        public void setCode3(int code3) {
            this.code3 = code3;
        }
        public float getFips() {
            return fips;
        }
        public void setFips(float fips) {
            this.fips = fips;
        }
        public LinkedList<Integer> getNewCases() {
            return newCases;
        }
        public void setNewCases(LinkedList<Integer> newCases) {
            this.newCases = newCases;
        }
        public LinkedList<Integer> getTotalCases() {
            return totalCases;
        }
        public void setTotalCases(LinkedList<Integer> totalCases) {
            this.totalCases = totalCases;
        }
    }

    // getters and setters for USTimeSeries
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LinkedList<String> getDates() {
        return dates;
    }
    public void setDates(LinkedList<String> dates) {
        this.dates = dates;
    }
    public HashMap<String, State> getStatesWithCountyData() {
        return statesWithCountyData;
    }
    public void setStatesWithCountyData(HashMap<String, State> statesWithCountyData) {
        this.statesWithCountyData = statesWithCountyData;
    }
}
