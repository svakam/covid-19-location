package com.vik.covid19vik;

import java.util.HashMap;
import java.util.LinkedList;

class USTimeSeries {
    private String status;
    private LinkedList<String> dates;
    private State[] statesWithCountyData;

    static class State {
        private String provinceOrState;
        private String countryOrRegion;
        HashMap<County, LinkedList<LinkedList<Integer>>> countyAndCases;

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
        public HashMap<County, LinkedList<LinkedList<Integer>>> getCountyAndCases() {
            return countyAndCases;
        }
        public void setCountyAndCases(HashMap<County, LinkedList<LinkedList<Integer>>> countyAndCases) {
            this.countyAndCases = countyAndCases;
        }
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
        private int fips;

        // getters and setters
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
        public float getUid() {
            return uid;
        }
        public void setUid(float uid) {
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
        public int getFips() {
            return fips;
        }
        public void setFips(int fips) {
            this.fips = fips;
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
    public State[] getStatesWithCountyData() {
        return statesWithCountyData;
    }
    public void setStatesWithCountyData(State[] statesWithCountyData) {
        this.statesWithCountyData = statesWithCountyData;
    }
}
