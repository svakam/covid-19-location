package com.vik.covid19vik;

import java.io.Serializable;
import java.util.Arrays;

public class Country implements Serializable {
    private String Country;
    private String Slug;
    private String[] Provinces;

    Country() {
        // no-args constructor
    }

    public Country(String country, String slug, String[] provinces) {
        this.Country = country;
        this.Slug = slug;
        this.Provinces = provinces;
    }

    public String getCountry() {
        return Country;
    }

    public String getSlug() {
        return Slug;
    }

    public String[] getProvinces() {
        return Provinces;
    }

    @Override
    public String toString() {
        return "Country='" + Country + '\'' +
                ", Slug='" + Slug + '\'' +
                ", Provinces=" + Arrays.toString(Provinces);
    }
}
