package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

class UIFMethods {
    static class UIFPopData {
        private float UID;
        private String iso2;
        private String iso3;
        private int code3;
        private int fips;
        private int population;
        private String combinedKey;
        private LinkedList<String> dropdown;

        // getters and setters
        float getUID() {
            return UID;
        }
        void setUID(float UID) {
            this.UID = UID;
        }
        String getIso2() {
            return iso2;
        }
        void setIso2(String iso2) {
            this.iso2 = iso2;
        }
        String getIso3() {
            return iso3;
        }
        void setIso3(String iso3) {
            this.iso3 = iso3;
        }
        int getCode3() {
            return code3;
        }
        void setCode3(int code3) {
            this.code3 = code3;
        }
        int getFips() {
            return fips;
        }
        void setFips(int fips) {
            this.fips = fips;
        }
        int getPopulation() {
            return population;
        }
        void setPopulation(int population) {
            this.population = population;
        }
        public String getCombinedKey() {
            return combinedKey;
        }
        public void setCombinedKey(String combinedKey) {
            this.combinedKey = combinedKey;
        }
        LinkedList<String> getDropdown() {
            return dropdown;
        }
        void setDropdown(LinkedList<String> dropdown) {
            this.dropdown = dropdown;
        }
    }

    static UIFPopData createCountryDropdownAndUIFPopData(HttpServletRequest req, String searchedCountry, CountryUIFLookup[] countries) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        if (countries != null) {
            UIFPopData uifPopData = new UIFPopData();
            System.out.println("API successfully pulled UIFLookup Info");
            CountryUIFLookup lastCountry = countries[countries.length - 1];
            for (CountryUIFLookup country : countries) {
                if (!countryDropdown.contains(country.getCountryOrRegion())) { // consider refactoring to use hashset to check if country name is contained in there
                    // country dropdown of names
                    countryDropdown.add(country.getCountryOrRegion());

                    if (country.getCountryOrRegion().equals(searchedCountry)) {
                        // add u/i/f and population data to object
                        uifPopData.setUID(country.getUid());
                        uifPopData.setIso2(country.getIso2());
                        uifPopData.setIso3(country.getIso3());
                        uifPopData.setCode3(country.getCode3());
                        uifPopData.setFips(country.getFips());
                        uifPopData.setPopulation(country.getPopulation());
                        uifPopData.setCombinedKey(country.getCombinedKey());
                    }
                }

                // since 80% of ApiMethods.getUIFLoop(req) is US data, stop as soon as country dropdown contains US with the assumption that the rest of data is US
                if (countryDropdown.contains("US") && lastCountry.getCountryOrRegion().equals("US")) {
                    break;
                }
            }
            uifPopData.setDropdown(countryDropdown);
            return uifPopData;
        } else {
            System.out.println("Unable to make API call to get UIF info.");
            return null;
        }
    }

    static LinkedList<String> createCountryDropdown(CountryUIFLookup[] countries) {
        if (countries != null) {
            LinkedList<String> countryDropdown = new LinkedList<>();
            System.out.println("API successfully pulled UIFLookup Info");

            // ------------- create dropdown ----------------//
            CountryUIFLookup lastCountry = countries[countries.length - 1];
            for (CountryUIFLookup country : countries) {
                if (!countryDropdown.contains(country.getCountryOrRegion())) {
                    // country dropdown of names
                    countryDropdown.add(country.getCountryOrRegion());
                }
                // since 80% of ApiMethods.getUIFLoop(req) is US data, stop as soon as country dropdown contains US with the assumption that the rest of data is US
                if (countryDropdown.contains("US") && lastCountry.getCountryOrRegion().equals("US")) {
                    break;
                }
            }
            return countryDropdown;
        } else {
            throw new NullPointerException("Unable to make API call to get UIF info.");
        }
    }

    static UIFPopData createCountryDropdownAndUIFPopDataProvince(String searchedProvince, CountryUIFLookup[] countries) {
        LinkedList<String> countryDropdown = new LinkedList<>();
        if (countries != null) {
            System.out.println("createCountryDropdownAndUIFPopDataProvince: API successfully pulled UIFLookup Info");
            UIFPopData uifPopData = new UIFPopData();
            HashSet<String> countriesSeen = new HashSet<>();
            CountryUIFLookup lastCountry = countries[countries.length - 1];
            int i;
            for (i = 0; i < countries.length; i++) {
                CountryUIFLookup country = countries[i];
                if (!countriesSeen.contains(country.getCountryOrRegion())) {
                    // country dropdown of names
                    countryDropdown.add(country.getCountryOrRegion());
                    countriesSeen.add(country.getCountryOrRegion());
                }
                if (country.getProvinceOrState().equals(searchedProvince)) {
                    // add u/i/f and population data to object
                    uifPopData.setUID(country.getUid());
                    uifPopData.setIso2(country.getIso2());
                    uifPopData.setIso3(country.getIso3());
                    uifPopData.setCode3(country.getCode3());
                    uifPopData.setFips(country.getFips());
                    uifPopData.setPopulation(country.getPopulation());
                    uifPopData.setCombinedKey(country.getCombinedKey());
                }
                // since 80% of ApiMethods.getUIFLoop(req) is US data, stop as soon as country dropdown contains US with the assumption that the rest of data is US
                if (countryDropdown.contains("US") && lastCountry.getCountryOrRegion().equals("US")) {
                    break;
                }
            }
            if (uifPopData.getCode3() == 0) {
                int j;
                for (j = i; j < countries.length - i; j++) {
                    if (countries[j].getProvinceOrState().equals(searchedProvince)) {
                        // add u/i/f and population data to object
                        uifPopData.setUID(countries[j].getUid());
                        uifPopData.setIso2(countries[j].getIso2());
                        uifPopData.setIso3(countries[j].getIso3());
                        uifPopData.setCode3(countries[j].getCode3());
                        uifPopData.setFips(countries[j].getFips());
                        uifPopData.setPopulation(countries[j].getPopulation());
                        uifPopData.setCombinedKey(countries[j].getCombinedKey());
                        break;
                    }
                }
            }
            uifPopData.setDropdown(countryDropdown);
            return uifPopData;
        } else {
            System.out.println("Unable to make API call to get UIF info.");
            return null;
        }
    }
}
