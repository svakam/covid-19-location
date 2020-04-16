package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedList;

class Dropdowns {
    static class UIFPopData {
        private int UID;
        private String iso2;
        private String iso3;
        private int code3;
        private int fips;
        private int population;
        private LinkedList<String> dropdown;

        // getters and setters
        public int getUID() {
            return UID;
        }
        public void setUID(int UID) {
            this.UID = UID;
        }
        public String getIso2() {
            return iso2;
        }
        public void setIso2(String iso2) {
            this.iso2 = iso2;
        }
        public String getIso3() {
            return iso3;
        }
        public void setIso3(String iso3) {
            this.iso3 = iso3;
        }
        public int getCode3() {
            return code3;
        }
        public void setCode3(int code3) {
            this.code3 = code3;
        }
        public int getFips() {
            return fips;
        }
        public void setFips(int fips) {
            this.fips = fips;
        }
        public int getPopulation() {
            return population;
        }
        public void setPopulation(int population) {
            this.population = population;
        }
        public LinkedList<String> getDropdown() {
            return dropdown;
        }
        public void setDropdown(LinkedList<String> dropdown) {
            this.dropdown = dropdown;
        }
    }

    protected static UIFPopData createCountryDropdownAndUIFPopData(HttpServletRequest req) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        CountryUIFLookup[] countries = ApiMethods.getUIFLookup(req);
        if (countries != null) {
            UIFPopData uifPopData = new UIFPopData();
            System.out.println("API successfully pulled UIFLookup Info");
            CountryUIFLookup lastCountry = countries[countries.length - 1];
            for (CountryUIFLookup country : countries) {
                if (!countryDropdown.contains(country.getCountryOrRegion())) {
                    // country dropdown of names
                    countryDropdown.add(country.getCountryOrRegion());

                    // add u/i/f and population data to object
                    uifPopData.setUID(country.getUid());
                    uifPopData.setIso2(country.getIso2());
                    uifPopData.setIso3(country.getIso3());
                    uifPopData.setCode3(country.getCode3());
                    uifPopData.setFips(country.getFips());
                    uifPopData.setPopulation(country.getPopulation());
                }
                // since 80% of ApiMethods.getUIFLoop(req) is US data, stop as soon as country dropdown contains US with the assumption that the rest of data is US
                if (countryDropdown.contains("US") && lastCountry.getCountryOrRegion().equals("US")) {
                    break;
                }
            }
            uifPopData.setDropdown(countryDropdown);
            return uifPopData;
        } else {
            throw new NullPointerException("Unable to make API call to get UIF info.");
        }
    }
}
