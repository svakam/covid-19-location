package com.vik.covid19vik;

import java.util.LinkedList;

class CountriesWithProvinceList {
    private String country;
    private LinkedList<String> allProvinces;

    // getters and setters
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public LinkedList<String> getAllProvinces() {
        return allProvinces;
    }
    public void setAllProvinces(LinkedList<String> allProvinces) {
        this.allProvinces = allProvinces;
    }


}
