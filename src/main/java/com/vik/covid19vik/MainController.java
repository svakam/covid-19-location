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

    // consider adding to database
    Country[] countriesArray;
    String[] countryNames;
    HashSet<String> countriesHashSet = new HashSet<>();
    HashMap<String, Array> countriesHashMap = new HashMap<>();

    // index
    @GetMapping("/")
    public String getIndex(Model model) {

        // GET request to /countries endpoint: contains country, slug, and array of provinces
        // deserializing JSON
        countriesArray = Country.getCountries();

        // array of country names
        countryNames = new String[countriesArray.length];
        for (int i = 0; i < countriesArray.length; i++) {
            countryNames[i] = countriesArray[i].getCountry();
        }

        // add deserialized JSON as attribute to index
        model.addAttribute("countries", countryNames);

        // ideally some code that caches result of JSONResult or stores in database, and checks to see if anything's changed after a day or since last update
        // would avoid redundant api call

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
