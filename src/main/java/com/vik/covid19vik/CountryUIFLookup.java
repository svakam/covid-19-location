package com.vik.covid19vik;

public class CountryUIFLookup {
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

    protected CountryUIFLookup() {
        // empty arg constructor
    }

    // getters
    protected int getUid() {
        return uid;
    }
    protected String getIso2() {
        return iso2;
    }
    protected String getIso3() {
        return iso3;
    }
    protected int getCode3() {
        return code3;
    }
    protected String getFips() {
        return fips;
    }
    protected String getCounty() {
        return county;
    }
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
    protected String getCombinedKey() {
        return combinedKey;
    }
    protected int getPopulation() {
        return population;
    }

    // setters
    protected void setUid(int uid) {
        this.uid = uid;
    }
    protected void setIso2(String iso2) {
        this.iso2 = iso2;
    }
    protected void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    protected void setCode3(int code3) {
        this.code3 = code3;
    }
    protected void setFips(String fips) {
        this.fips = fips;
    }
    protected void setCounty(String county) {
        this.county = county;
    }
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
    protected void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }
    protected void setPopulation(int population) {
        this.population = population;
    }
}
