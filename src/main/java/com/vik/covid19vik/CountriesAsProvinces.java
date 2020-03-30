package com.vik.covid19vik;

import java.util.HashMap;

public class CountriesAsProvinces {
    public static HashMap<String, String> getCountriesAsProvinces() {
        // hashmap details countries that are also provinces of a country
        HashMap<String, String> countriesAsProvinces = new HashMap<>();
        countriesAsProvinces.put("Aruba", "Netherlands");
        countriesAsProvinces.put("Curacao", "Netherlands");
        countriesAsProvinces.put("Netherlands", "Netherlands");
        countriesAsProvinces.put("France", "France");
        countriesAsProvinces.put("French Guiana", "France");
        countriesAsProvinces.put("Saint Martin", "France");
        countriesAsProvinces.put("Mayotte", "France");
        countriesAsProvinces.put("Guadeloupe", "France");
        countriesAsProvinces.put("Reunion", "France");
        countriesAsProvinces.put("Martinique", "France");
        countriesAsProvinces.put("Denmark", "Denmark");
        countriesAsProvinces.put("Faroe Islands", "Denmark");
        countriesAsProvinces.put("Greenland", "Denmark");
        countriesAsProvinces.put("UK", "UK"); // UK country data is currently undergoing changes
        countriesAsProvinces.put("Gibraltar", "United Kingdom");
        countriesAsProvinces.put("Channel Islands", "United Kingdom");
        countriesAsProvinces.put("Cayman Islands", "United Kingdom");
        countriesAsProvinces.put("Hong Kong", "China");
        countriesAsProvinces.put("Macau", "China");
        return countriesAsProvinces;
    }

//    public static void orderOfDataForProvince() {
//        HashMap<String, String[]> orderOfData = new HashMap<>();
//        orderOfData.put("Aruba", new String[]{"Aruba", "country"});
//        orderOfData.put("Curacao", new String[]{"country", "Curacao"});
//        orderOfData.put("French Guiana", new String[]{"country", ""});
//        orderOfData.put("Saint Martin", new String[]{
//    }

    // methods to match
}
