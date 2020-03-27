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

    // index
    @GetMapping("/")
    public String getIndex(Model model) {

        // GET request to /countries endpoint: redundant list contains country, slug, and array of provinces
        // deserializing JSON
        countriesArray = Country.getCountries();

        countryNames = RedundantCountryMethods.filterRedundantCountries(countriesArray);

        // ideally some code that caches result of JSONResult or stores in database, and checks to see if anything's changed after a day or since last update
        // would avoid repetitive api call

        model.addAttribute("countryNames", countryNames);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results")
    public RedirectView submitSearch(String searchedCountry) {

        System.out.println("Dropdown selected = " + searchedCountry);
        RedirectView rv;

        // if a redundant country, access the redundant country hashmap, get all its slugs in proper order and return view with slugs passed in with country
        HashMap<String, String[]> redundantCountriesWithSlugs = RedundantCountryMethods.getRedundantCountriesWithSlugs();
        if (redundantCountriesWithSlugs.containsKey(searchedCountry)) {
            String[] allSlugsForCountry = redundantCountriesWithSlugs.get(searchedCountry);
            StringBuilder slugs = new StringBuilder();
            for (String slug : allSlugsForCountry) {
                slugs.append("&").append(slug);
            }
            rv = new RedirectView("/results/" + searchedCountry + "+" + slugs);

        } // else find slug for country
        else {
            String slug = null;
            for (Country country : countriesArray) {
                if (searchedCountry.equals(country.getCountry())) {
                    slug = country.getSlug();
                }
            }
            if (slug == null) {
                return new RedirectView("/error/404/" + searchedCountry);
            }
            rv = new RedirectView("/results/" + searchedCountry + "+" + slug);
        }

        return rv;
    }

    @GetMapping("/results/{searchedCountry}+{slug}")
    public String allResults(@PathVariable String searchedCountry, @PathVariable String slug, Model model) {

        System.out.println(slug);
        System.out.println(searchedCountry);

        // GET request to /summary endpoint: contains country, slug, and case info on country level
        // deserializes JSON
        HashMap<String, int[]> summaryCasesByCountry = SummaryCasesByCountry.getCountriesCases();

        int[] caseInfoForCountry;

        // if searched country is redundant, get all slugs, total case data up appropriately, and add to model
        HashMap<String, String[]> redundantCountriesWithSlugs = RedundantCountryMethods.getRedundantCountriesWithSlugs();
        if (redundantCountriesWithSlugs.containsKey(searchedCountry)) {
            caseInfoForCountry = new int[]{0,0,0,0,0,0};
            String[] slugs = redundantCountriesWithSlugs.get(searchedCountry);
            for (String redundantSlug : slugs) {
                System.out.println("redundantSlug = " + redundantSlug);
                int[] caseInfoPerSlug = summaryCasesByCountry.get(redundantSlug);
                for (int i = 0; i < caseInfoPerSlug.length; i++) {
                    caseInfoForCountry[i] += caseInfoPerSlug[i];
                }
            }
        } // else get case data for slug
        else {
            caseInfoForCountry = summaryCasesByCountry.get(slug);
        }

        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);

        return "results";
    }

    // error for slug
    @GetMapping("/error/404/{searchedCountry}")
    public String error404(@PathVariable String searchedCountry, Model model) {
        model.addAttribute(searchedCountry);
        return "error404";
    }
}
