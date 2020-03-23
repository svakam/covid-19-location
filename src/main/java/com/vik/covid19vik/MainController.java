package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;


import java.io.*;
import java.util.HashSet;

@Controller
public class MainController {

    Country[] countriesArray;
    HashSet<String> countriesHashSet = new HashSet<>();
    Country countryInfo;

    // index
    @GetMapping("/")
    public String getIndex(Model model) throws IOException {
        // GET request to /countries endpoint: contains country, slug, and array of provinces
        countriesArray = Country.getCountries();

        // add countries to hashset
        for (Country country : countriesArray) {
            countriesHashSet.add(country.getCountry());
        }

        // add deserialized JSON as attribute to index
        model.addAttribute("countries", countriesArray);

        // ideally some code that caches result of JSONResult or stores in database, and checks to see if anything's changed after a day or since last update
        // would avoid redundant api call

        return "index";
    }

    // post request with user's search
    @PostMapping("/results")
    public RedirectView submitSearch(String searchedCountry) {

        // submit form input with country info
        System.out.println("Dropdown selected = " + searchedCountry);

        String slug = null;

        // find slug for country
        for (Country country : countriesArray) {
            if (searchedCountry.equals(country.getCountry())) {
                slug = country.getSlug();
            }
        }

        // some kind of error checking if slug comes back as null

        return new RedirectView("/results/" + slug);
    }

    @GetMapping("/results/{slug}")
    public String allResults(@PathVariable String slug, Model model) {
        System.out.println(slug);
        model.addAttribute("slug", slug);

        return "results";
    }
}
