package com.vik.covid19vik;

public class CountryUIFLookup {
    private int uid;
    private String iso2;
    private String iso3;
    private String code3;
    private String fips;
    private String county;
    private String provinceOrState;
    private String countryOrRegion;
    private float lat;
    private float lon;
    private String combinedKey;
    private String population;

    public CountryUIFLookup() {
        // empty arg constructor
    }

    // getters
    public int getUid() {
        return uid;
    }
    public String getIso2() {
        return iso2;
    }
    public String getIso3() {
        return iso3;
    }
    public String getCode3() {
        return code3;
    }
    public String getFips() {
        return fips;
    }
    public String getCounty() {
        return county;
    }
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
    public String getCombinedKey() {
        return combinedKey;
    }
    public String getPopulation() {
        return population;
    }

    // setters
    public void setUid(int uid) {
        this.uid = uid;
    }
    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }
    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    public void setCode3(String code3) {
        this.code3 = code3;
    }
    public void setFips(String fips) {
        this.fips = fips;
    }
    public void setCounty(String county) {
        this.county = county;
    }
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
    public void setCombinedKey(String combinedKey) {
        this.combinedKey = combinedKey;
    }
    public void setPopulation(String population) {
        this.population = population;
    }
}
