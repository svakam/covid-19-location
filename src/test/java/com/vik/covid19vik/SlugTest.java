package com.vik.covid19vik;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SlugTest {
    @Test
    public void testHashMap() {
        HashMap<String[], Boolean> allSlugsWithBooleans = RedundantCountryMethods.getRedundantSlugsCheck();
        HashMap<String, String[]> redundantSlugsExpected = new HashMap<>();
        HashMap<String, String[]> redundantSlugs = new HashMap<>();
        Set<String[]> allSlugs = allSlugsWithBooleans.keySet();
        for (String[] slugSet : allSlugs) {
            for (String slug : slugSet) {
                redundantSlugsExpected.put(slug, slugSet);
            }
        }

        redundantSlugs.put("-azerbaijan", new String[]{"-azerbaijan", "azerbaijan"});
        redundantSlugs.put("azerbaijan", new String[]{"-azerbaijan", "azerbaijan"});
        redundantSlugs.put("the-bahamas", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
        redundantSlugs.put("bahamas-the", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
        redundantSlugs.put("bahamas", new String[]{"the-bahamas", "bahamas-the", "bahamas"});
        redundantSlugs.put("the-gambia", new String[]{"the-gambia", "gambia-the", "gambia"});
        redundantSlugs.put("gambia-the", new String[]{"the-gambia", "gambia-the", "gambia"});
        redundantSlugs.put("gambia", new String[]{"the-gambia", "gambia-the", "gambia"});
        redundantSlugs.put("hong-kong", new String[]{"hong-kong", "hong-kong-sar"});
        redundantSlugs.put("hong-kong-sar", new String[]{"hong-kong", "hong-kong-sar"});
        redundantSlugs.put("iran", new String[]{"iran", "iran-(islamic-republic-of)"});
        redundantSlugs.put("iran-(islamic-republic-of)", new String[]{"iran", "iran-(islamic-republic-of)"});
        redundantSlugs.put("russia", new String[]{"russia", "russian-federation"});
        redundantSlugs.put("russian-federation", new String[]{"russia", "russian-federation"});
        redundantSlugs.put("south-korea", new String[]{"south-korea", "republic-of-korea", "korea-south"});
        redundantSlugs.put("republic-of-korea", new String[]{"south-korea", "republic-of-korea", "korea-south"});
        redundantSlugs.put("korea-south", new String[]{"-azerbaijan", "azerbaijan"});
        redundantSlugs.put("taiwan", new String[]{"taiwan", "taipei-and-environs"});
        redundantSlugs.put("taipei-and-environs", new String[]{"taiwan", "taipei-and-environs"});
        redundantSlugs.put("uk", new String[]{"uk", "united-kingdom"});
        redundantSlugs.put("united-kingdom", new String[]{"uk", "united-kingdom"});
        redundantSlugs.put("vietnam", new String[]{"vietnam", "viet-nam"});
        redundantSlugs.put("viet-nam", new String[]{"vietnam", "viet-nam"});
    }

    @Test
    public void testGetRedCountriesWithSlugs() {
        HashMap<String, String[]> a = RedundantCountryMethods.getRedundantCountriesWithSlugs();
        System.out.println(a.keySet());
        System.out.println(a.entrySet());
    }

    @Test
    public void testGetRedundantSlugsCheck() {
        HashMap<String[], Boolean> b = RedundantCountryMethods.getRedundantSlugsCheck();
        System.out.println(b.keySet());
        System.out.println(b.entrySet());
    }

    @Test
    public void testGetRedundantCountries() {
        HashSet<String> c = RedundantCountryMethods.getRedundantCountries();
        for (String slug : c) {
            System.out.println(slug);
        }
    }
}
