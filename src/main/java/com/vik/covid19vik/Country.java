package com.vik.covid19vik;

import java.util.LinkedList;

public class Country {
    private String provinceOrState;
    private String countryOrRegion;
    private float lat;
    private float lon;
    private String status;
    private LinkedList<String> dates;
    private LinkedList<Integer> cases;

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
    public String getStatus() {
        return status;
    }
    public LinkedList<String> getDates() {
        return dates;
    }
    public LinkedList<Integer> getCases() {
        return cases;
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
