package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.lang.reflect.Array;
import java.util.*;

@Controller
public class MainController {

    // consider adding to database:

    // array of all countries with country names/slug/province
    Country[] countriesArray;
    // array of country names for country dropdown
    LinkedList<String> countryNames;

    // hashmap with country as key and instance of a hashset of provinces/states in country
    HashMap<String, HashSet<String>> countryProvinceHashMap = new HashMap<>();
    // array of province/state names for province/state dropdown
    LinkedList<String> provinceNames;
    // new hashset instance for every country
    HashSet<String> provincesForCountry;

    // index
    @GetMapping("/")
    public String getIndex(Model model) {

        // GET request to /countries endpoint: redundant list contains country, slug, and array of provinces
        // deserializing JSON
        countriesArray = Country.getCountries();

        countryNames = new LinkedList<>();

        HashMap<String[], Boolean> checkIfCountryAddedAlready = RedundantCountryMethods.getRedundantSlugsCheck();

        // add country names to a country dropdown list:
        // iterate over raw countries array
        // if a country is redundant, don't add it to the dropdown list:
        // get the slug of ith country
        // if that slug is contained in a list of redundant countries, get the slug's associated redundancies and check if it was already added (true/false flag)

        for (Country country : countriesArray) {
            // if a country has already been added to the dropdown, don't add the redundant country to the dropdown list
            String slug = country.getSlug();
            HashSet<String> redundantCountryList = RedundantCountryMethods.getRedundantCountries();
            if (redundantCountryList.contains(slug)) {
                HashMap<String, String[]> redundantSlugs = RedundantCountryMethods.getRedundantSlugs();
                String[] redundanciesForACountry = redundantSlugs.get(slug);
                // if slug's country hasn't been added to dropdown yet (flag == false), add it and switch flag to true,
                if (checkIfCountryAddedAlready.containsKey(redundanciesForACountry) && !checkIfCountryAddedAlready.get(redundanciesForACountry)) {
                    countryNames.add(country.getCountry());
                    checkIfCountryAddedAlready.put(redundanciesForACountry, true);
                }
                // else don't add it
            } else {
                countryNames.add(country.getCountry());
            }
        }



        // ideally some code that caches result of JSONResult or stores in database, and checks to see if anything's changed after a day or since last update
        // would avoid redundant api call

        model.addAttribute("countryNames", countryNames);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results")
    public RedirectView submitSearch(String searchedCountry) {

        System.out.println("Dropdown selected = " + searchedCountry);

        // find slug for country
        String slug = null;
        for (Country country : countriesArray) {
            if (searchedCountry.equals(country.getCountry())) {
                slug = country.getSlug();
            }
        }

        // some kind of error checking if slug comes back as null

        return new RedirectView("/results/" + searchedCountry + "&" + slug);
    }

    @GetMapping("/results/{searchedCountry}&{slug}")
    public String allResults(@PathVariable String slug, @PathVariable String searchedCountry, Model model) {

        System.out.println(slug);
        System.out.println(searchedCountry);

        model.addAttribute("slug", slug);
        model.addAttribute("searchedCountry", searchedCountry);

        // GET request to /summary endpoint: contains country, slug, and case info on country level
        // deserializes JSON
        HashMap<String, int[]> summaryCasesByCountry = SummaryCasesByCountry.getCountriesCases();

        // use slug to access a hashmap<country, array> where country is slugs and array is an order of types of cases for that country
        int[] caseInfoForCountry = summaryCasesByCountry.get(slug);
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);

        return "results";
    }
}
