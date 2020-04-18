package com.vik.covid19vik;

public class CountryUIFLookup {
    private int uid;
    private String iso2;
    private String iso3;
    private int code3;
    private int fips;
    private String county;
    private String provinceOrState;
    private String countryOrRegion;
    private float lat;
    private float lon;
    private String combinedKey;
    private int population;

    CountryUIFLookup() {
        // empty arg constructor
    }

    // getters
    int getUid() {
        return uid;
    }
    String getIso2() {
        return iso2;
    }
    String getIso3() {
        return iso3;
    }
    int getCode3() {
        return code3;
    }
    int getFips() {
        return fips;
    }
    String getCounty() {
        return county;
    }
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
    String getCombinedKey() {
        return combinedKey;
    }
    int getPopulation() {
        return population;
    }

    // setters
    void setUid(int uid) {
        this.uid = uid;
    }
    void setIso2(String iso2) {
        this.iso2 = iso2;
    }
    void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    void setCode3(int code3) {
        this.code3 = code3;
    }
    void setFips(int fips) {
        this.fips = fips;
    }
    void setCounty(String county) {
        this.county = county;
    }
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
    void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }
    void setPopulation(int population) {
        this.population = population;
    }
}
