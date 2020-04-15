package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;

class Dropdowns {
    static LinkedList<String> createCountryDropdown(HttpServletRequest req) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        CountryUIFLookup[] countries = ApiMethods.getUIFLookup(req);
        if (countries != null) {
            System.out.println("API successfully pulled UIFLookup Info");
            for (CountryUIFLookup country : countries) {
                // country dropdown of names
                if (!countryDropdown.contains(country.getCountryOrRegion())) {
                    countryDropdown.add(country.getCountryOrRegion());
                }
            }
        } else {
            throw new NullPointerException("Unable to make API call to get UIF info.");
        }
        return countryDropdown;
    }
}
