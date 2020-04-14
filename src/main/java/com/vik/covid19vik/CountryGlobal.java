package com.vik.covid19vik;

import java.util.LinkedList;

public class CountryGlobal {
    private LinkedList<String> dates;
    private String status;
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
    public void setDates(LinkedList<String> dates) {
        this.dates = dates;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCountries(LinkedList<Country> countries) {
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
        public LinkedList<Integer> getTotalCases() {
            return totalCases;
        }
        public LinkedList<Integer> getNewCases() {
            return newCases;
        }

        // setters
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
        public void setTotalCases(LinkedList<Integer> totalCases) {
            this.totalCases = totalCases;
        }
        public void setNewCases(LinkedList<Integer> newCases) {
            this.newCases = newCases;
        }
    }

}
