package com.vik.covid19vik;

import java.util.Arrays;

public class Countries {
    private String Country;
    private String Slug;
    private String[] Provinces;

    Countries() {
        // no-args constructor
    }

    @Override
    public String toString() {
        return "Countries{" +
                "Country='" + Country + '\'' +
                ", Slug='" + Slug + '\'' +
                ", Provinces=" + Arrays.toString(Provinces) +
                '}';
    }
}
