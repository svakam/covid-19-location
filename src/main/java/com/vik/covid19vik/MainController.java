package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class MainController {

    // consider adding to database:

    // GET request to /countries endpoint returns list of countries (name, slug, provinces)
    // JSON deserialization
    AllCountries[] countriesArray = AllCountries.getCountries();

    // extract only country names for country dropdown menu
    LinkedList<String> countryNames = RedundantCountryMethods.filterRedundantCountries(countriesArray);

    // index
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("countryNames", countryNames);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results/country")
    public RedirectView submitCountrySearch(String searchedCountry) throws UnsupportedEncodingException {

        System.out.println("Dropdown selected = " + searchedCountry);

        RedirectView rv;

        // if a redundant country, access the redundant country hashmap, get all its slugs in proper order and return view with slugs passed in with country
        HashMap<String, String[]> redundantCountriesWithSlugs = RedundantCountryMethods.getRedundantCountriesWithSlugs();
        if (redundantCountriesWithSlugs.containsKey(searchedCountry)) {
            String[] allSlugsForCountry = redundantCountriesWithSlugs.get(searchedCountry);
            StringBuilder slug = new StringBuilder();
            for (int i = 0; i < allSlugsForCountry.length; i++) {
                if (i != allSlugsForCountry.length - 1) {
                    slug.append(URLEncoder.encode(allSlugsForCountry[i], "UTF-8")).append(",");
                } else {
                    slug.append(URLEncoder.encode(allSlugsForCountry[i], "UTF-8"));
                }
            }
            rv = new RedirectView("/results/country?sc=" + searchedCountry + "&slug=" + slug);
        }

        // else if searched country is also a province, pass its slug along with the country it's a province of
        else if (CountriesAsProvinces.countriesAsProvinces().containsKey(searchedCountry)) {
            String slug = null;
            for (AllCountries country : countriesArray) {
                if (searchedCountry.equals(country.getCountry())) {
                    slug = country.getSlug();
                }
            }
            String countryOfProvince = CountriesAsProvinces.countriesAsProvinces().get(searchedCountry);
            rv = new RedirectView("/results/country?sc=" + searchedCountry + "&slug=" + slug + "&cop=" + countryOfProvince);
        }

        // else find slug for country
        else {
            String slug = null;
            for (AllCountries allCountries : countriesArray) {
                if (searchedCountry.equals(allCountries.getCountry())) {
                    slug = allCountries.getSlug();
                }
            }
            if (slug == null) {
                return new RedirectView("/error/404/" + searchedCountry);
            }
            rv = new RedirectView("/results/country?sc=" + searchedCountry + "&slug=" + slug);
        }

        return rv;
    }

//    @PostMapping("/results/country/province")
//    public RedirectView submitProvinceSearch(String province) {
//        RedirectView rv;
//        return rv;
//    }

    // path variable always searchedCountry
    // possible params: slug: not required (if not redundant or a province), slugs: not required (if redundant), slug AND countryOfProvince: not required (if also a province)
    @GetMapping("/results/country")
    public String resultsForCountry(@RequestParam(name = "sc") String searchedCountry, @RequestParam(required = false, name = "slug")
            String slug, @RequestParam(required = false, name = "cop") String countryOfProvince, Model model) {
        System.out.println("slug = " + slug);
        System.out.println("searched country = " + searchedCountry);
        System.out.println("country of province = " + countryOfProvince);

        // GET request to /summary endpoint: contains country, slug, and case info on country level
        // deserializes JSON
        HashMap<String, int[]> summaryCasesByCountry = SummaryCasesByCountry.getCountriesCases();

        int[] caseInfoForCountry;

        // if searched country is redundant, get all slugs, get case data for last slug and pass to template
        if (RedundantCountryMethods.getRedundantCountriesWithSlugs().containsKey(searchedCountry)) {
            HashMap<String, String[]> redundantCountriesWithSlugs = RedundantCountryMethods.getRedundantCountriesWithSlugs();
            String[] allSlugs = redundantCountriesWithSlugs.get(searchedCountry);
            String slugWithMostRelevantData = allSlugs[allSlugs.length - 1];
            caseInfoForCountry = summaryCasesByCountry.get(slugWithMostRelevantData);
        }
//        // else if searched country is also a province, fetch all data associated with slug
//        else if () {
//
//        }
        // else get case data for slug
        else {
            caseInfoForCountry = summaryCasesByCountry.get(slug);
        }

        // get list of provinces to pass in dropdown
        // error check for empty data set: if null, pass in error

        // dropdown for country
        model.addAttribute("countryNames", countryNames);
        // dropdown for provinces

        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);
        model.addAttribute("countryOfProvince", countryOfProvince);

        return "countryResults";
    }

    @GetMapping("/results/country/province")
    public String resultsForProvince(@RequestParam(name = "sp") String searchedProvince, @RequestParam(required = false, name = "slug") String slug, Model model) {

        model.addAttribute("countryNames", countryNames);
        return "provinceResults";
    }

    // error for slug
    @GetMapping("/error/404/{searchedCountry}")
    public String error404(@PathVariable String searchedCountry, Model model) {
        model.addAttribute(searchedCountry);
        model.addAttribute("countryNames", countryNames);
        return "error404";
    }

    // fallback
    @GetMapping("*")
    public String fallback(Model model) {
        model.addAttribute("countryNames", countryNames);
        return "error404";
    }
}
