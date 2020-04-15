package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;

class Dropdowns {
    protected static LinkedList<String> createCountryDropdownAndUIFPopData(HttpServletRequest req) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        CountryUIFLookup[] countries = ApiMethods.getUIFLookup(req);
        if (countries != null) {
            System.out.println("API successfully pulled UIFLookup Info");
            CountryUIFLookup lastCountry = countries[countries.length - 1];
            for (CountryUIFLookup country : countries) {
                // country dropdown of names
                if (!countryDropdown.contains(country.getCountryOrRegion())) {
                    countryDropdown.add(country.getCountryOrRegion());
                }
                if (countryDropdown.contains("US") && lastCountry.getCountryOrRegion().equals("US")) {
                    break;
                }
            }
        } else {
            throw new NullPointerException("Unable to make API call to get UIF info.");
        }
        return countryDropdown;
    }
}
