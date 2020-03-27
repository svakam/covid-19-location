package com.vik.covid19vik;

import java.util.*;

public class RedundantCountryMethods {

    // used to get a country name and get order of slugs that represents corrected order of data
    public static HashMap<String, String[]> getRedundantCountriesWithSlugs() {
        // redundant country hashmap: country name as key and array of possible slugs as values
        HashMap<String, String[]> redundantCountriesWithSlugs = new HashMap<>();
        redundantCountriesWithSlugs.put("Azerbaijan", new String[]{"-azerbaijan", "azerbaijan"});
        redundantCountriesWithSlugs.put("Bahamas, The", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
        redundantCountriesWithSlugs.put("Gambia, The", new String[]{"the-gambia", "gambia-the", "gambia"});
        redundantCountriesWithSlugs.put("Hong Kong", new String[]{"hong-kong", "hong-kong-sar"});
        redundantCountriesWithSlugs.put("Iran", new String[]{"iran", "iran-(islamic-republic-of)"});
        redundantCountriesWithSlugs.put("Russia", new String[]{"russia", "russian-federation"});
        redundantCountriesWithSlugs.put("South Korea", new String[]{"south-korea", "republic-of-korea", "korea-south"});
        redundantCountriesWithSlugs.put("Taiwan", new String[]{"taiwan", "taipei-and-environs"});
        redundantCountriesWithSlugs.put("United Kingdom", new String[]{"uk", "united-kingdom"});
        redundantCountriesWithSlugs.put("Vietnam", new String[]{"vietnam", "viet-nam"});
        return redundantCountriesWithSlugs;
    }

    // used to check if country dropdown was previously populated with an already existing country
    public static HashMap<String[], Boolean> getRedundantSlugsCheck() {
        // redundant slug check hashmap: array of associated slugs as key and incrementer as value
        HashMap<String[], Boolean> redundantSlugsCheck = new HashMap<>();
        redundantSlugsCheck.put(new String[]{"-azerbaijan", "azerbaijan"}, false);
        redundantSlugsCheck.put(new String[]{"the-bahamas", "bahamas-the", "bahamas"}, false);
        redundantSlugsCheck.put(new String[]{"the-gambia", "gambia-the", "gambia"}, false);
        redundantSlugsCheck.put(new String[]{"hong-kong", "hong-kong-sar"}, false);
        redundantSlugsCheck.put(new String[]{"iran", "iran-(islamic-republic-of)"}, false);
        redundantSlugsCheck.put(new String[]{"russia", "russian-federation"}, false);
        redundantSlugsCheck.put(new String[]{"south-korea", "republic-of-korea", "korea-south"}, false);
        redundantSlugsCheck.put(new String[]{"taiwan", "taipei-and-environs"}, false);
        redundantSlugsCheck.put(new String[]{"uk", "united-kingdom"}, false);
        redundantSlugsCheck.put(new String[]{"vietnam", "viet-nam"}, false);
        return redundantSlugsCheck;
    }

    // list of all possible slugs for redundant countries
    public static HashSet<String> getRedundantCountries() {
        // hashset with all possible slugs for all redundant countries
        HashSet<String> redundantCountries = new HashSet<>();

        HashMap<String, String[]> countriesWithSlugs = getRedundantCountriesWithSlugs();
        // iterate over hashmap, add every slug to this hashset
        for (Map.Entry<String, String[]> entry : countriesWithSlugs.entrySet()) {
            redundantCountries.addAll(Arrays.asList(entry.getValue()));
        }
        return redundantCountries;
    }

    // list of all possible slugs as keys with array of their associated redundant slugs as values
    public static HashMap<String, String[]> getRedundantSlugs() {
        // redundant slug hashmap: slug as key and array of associated slugs as values
        HashMap<String, String[]> redundantSlugs = new HashMap<>();

        HashMap<String[], Boolean> allSlugsWithBooleans = getRedundantSlugsCheck();
        Set<String[]> allSlugsSet = allSlugsWithBooleans.keySet();
        for (String[] slugSet : allSlugsSet) {
            for (String slug : slugSet) {
                redundantSlugs.put(slug, slugSet);
            }
        }

//        redundantSlugs.put("-azerbaijan", new String[]{"-azerbaijan", "azerbaijan"});
//        redundantSlugs.put("azerbaijan", new String[]{"-azerbaijan", "azerbaijan"});
//        redundantSlugs.put("the-bahamas", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
//        redundantSlugs.put("bahamas-the", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
//        redundantSlugs.put("bahamas", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
//        redundantSlugs.put("the-gambia", new String[]{"the-gambia", "gambia-the", "gambia"});
//        redundantSlugs.put("gambia-the", new String[]{"the-gambia", "gambia-the", "gambia"});
//        redundantSlugs.put("gambia", new String[]{"the-gambia", "gambia-the", "gambia"});
//        redundantSlugs.put("hong-kong", new String[]{"hong-kong", "hong-kong-sar"});
//        redundantSlugs.put("hong-kong-sar", new String[]{"hong-kong", "hong-kong-sar"});
//        redundantSlugs.put("iran", new String[]{"iran", "iran-(islamic-republic-of)"});
//        redundantSlugs.put("iran-(islamic-republic-of)", new String[]{"iran", "iran-(islamic-republic-of)"});
//        redundantSlugs.put("russia", new String[]{"russia", "russian-federation"});
//        redundantSlugs.put("russian-federation", new String[]{"russia", "russian-federation"});
//        redundantSlugs.put("south-korea", new String[]{"south-korea", "republic-of-korea", "korea-south"});
//        redundantSlugs.put("republic-of-korea", new String[]{"south-korea", "republic-of-korea", "korea-south"});
//        redundantSlugs.put("korea-south", new String[]{"-azerbaijan", "azerbaijan"});
//        redundantSlugs.put("taiwan", new String[]{"taiwan", "taipei-and-environs"});
//        redundantSlugs.put("taipei-and-environs", new String[]{"taiwan", "taipei-and-environs"});
//        redundantSlugs.put("uk", new String[]{"uk", "united-kingdom"});
//        redundantSlugs.put("united-kingdom", new String[]{"uk", "united-kingdom"});
//        redundantSlugs.put("vietnam", new String[]{"vietnam", "viet-nam"});
//        redundantSlugs.put("viet-nam", new String[]{"vietnam", "viet-nam"});
        return redundantSlugs;
    }
}
