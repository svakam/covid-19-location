package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

class Dropdowns {
    static LinkedList<String> createCountryDropdown(HttpServletRequest req) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        CountryUIFLookup[] countries = ApiMethods.getUIFLookup(req);
        if (countries != null) {
            for (CountryUIFLookup country : countries) {

                // country dropdown of names
                if (!countryDropdown.contains(country.getCountryOrRegion())) {
                    countryDropdown.add(country.getCountryOrRegion());
                }

                // find searched country
            }
        } else {
            throw new NullPointerException("Unable to make API call to get UIF info.");
        }
        return countryDropdown;
    }
}
