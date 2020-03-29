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
        else if (CountriesAsProvinces.getCountriesAsProvinces().containsKey(searchedCountry)) {
            String slug = null;
            for (AllCountries country : countriesArray) {
                if (searchedCountry.equals(country.getCountry())) {
                    slug = country.getSlug();
                }
            }
            String countryOfProvince = CountriesAsProvinces.getCountriesAsProvinces().get(searchedCountry);
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

    @PostMapping("/results/country/province")
    public RedirectView submitProvinceSearch(String searchedProvince, String countryForProvince) {
        RedirectView rv;
        System.out.println("searched country = " + countryForProvince);
        String slug = null;
        for (AllCountries country : countriesArray) {
            if (countryForProvince.equals(country.getCountry())) {
                slug = country.getSlug();
                System.out.println("slug of country = " + slug);
            }
        }
        rv = new RedirectView("/results/country/province" + "?sc=" + countryForProvince + "&sp=" + searchedProvince + "&slug=" + slug);
        return rv;
    }

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

        // summary data initialize
        int[] caseInfoForCountry;
        // dropdown data initialize
        String[] provinceNames = null;

        // if searched country is redundant, get all slugs, get case data for last slug and pass to template
        if (RedundantCountryMethods.getRedundantCountriesWithSlugs().containsKey(searchedCountry)) {
            System.out.println("redundant slug = " + slug);

            // store last slug from string of slugs in URL and get summary case info by that slug
            Queue<Character> findLastSlugInURL = new LinkedList<>();
            StringBuilder slugWithMostRelevantSummary = new StringBuilder();
            for (int i = 0; i < slug.length(); i++) {
                if (slug.charAt(i) != ',') {
                    findLastSlugInURL.add(slug.charAt(i));
                } else {
                    while (findLastSlugInURL.peek() != null) {
                        findLastSlugInURL.poll();
                    }
                }
            }
            while (findLastSlugInURL.peek() != null) {
                slugWithMostRelevantSummary.append(findLastSlugInURL.poll());
            }
            caseInfoForCountry = summaryCasesByCountry.get(slugWithMostRelevantSummary.toString());

            // populate province dropdown
            for (AllCountries country : countriesArray) {
                if (slugWithMostRelevantSummary.toString().equals(country.getSlug())) {
//                    provinceNames = country.getProvinces();
                }
            }
        }

        // else if searched country is also a province, fetch all data associated with slug
        else if (CountriesAsProvinces.getCountriesAsProvinces().containsKey(searchedCountry)) {
            System.out.println('h');
            caseInfoForCountry = summaryCasesByCountry.get(slug);
            for (AllCountries country : countriesArray) {
                if (slug.equals(country.getSlug())) {
//                    provinceNames = country.getProvinces();
                }
            }
        }

        // else get case data for slug
        else {
            caseInfoForCountry = summaryCasesByCountry.get(slug);

            // get list of provinces to pass in dropdown
            for (AllCountries country : countriesArray) {
                if (slug.equals(country.getSlug())) {
                    provinceNames = country.getProvinces();
                }
            }
        }

        // error check for empty data set: if null, pass in error

        // dropdown for country
        model.addAttribute("countryNames", countryNames);
        // dropdown for provinces
        model.addAttribute("provinceNames", provinceNames);

        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);
        model.addAttribute("countryOfProvince", countryOfProvince);

        return "countryResults";
    }

    @GetMapping("/results/country/province")
    public String resultsForProvince(@RequestParam(name = "sc") String searchedCountry, @RequestParam(name = "sp")
            String searchedProvince, @RequestParam(required = false, name = "slug") String slug, Model model) {

        System.out.println(searchedProvince);
        System.out.println(slug);

        LinkedList<String> provinceNames;

        // instantiate storage for case data
        LinkedList<Integer> timeSeriesConfirmedCases = new LinkedList<>();
        LinkedList<Integer> timeSeriesDeathsCases = new LinkedList<>();
        LinkedList<Integer> timeSeriesRecoveredCases = new LinkedList<>();
        LinkedList<String> confirmedDates = new LinkedList<>();
        LinkedList<String> deathsDates = new LinkedList<>();
        LinkedList<String> recoveredDates = new LinkedList<>();

        // if searched country is redundant, get all slugs, get case data for last slug and pass to template
        if (RedundantCountryMethods.getRedundantCountriesWithSlugs().containsKey(searchedCountry)) {

            // parse out all slugs as strings and add to list
            LinkedList<String> allSlugs = new LinkedList<>();
            Queue<Character> slugProcessor = new LinkedList<>();
            StringBuilder givenSlug = null;
            for (int i = 0; i < slug.length(); i++) {
                if (slug.charAt(i) != ',') {
                    slugProcessor.add(slug.charAt(i));
                } else {
                    givenSlug = new StringBuilder();
                    while (slugProcessor.peek() != null) {
                        givenSlug.append(slugProcessor.poll());
                    }
                    allSlugs.add(givenSlug.toString());
                }
            }

            CountryAndProvincesData[] timeSeriesConfirmed;
            CountryAndProvincesData[] timeSeriesDeaths;
            CountryAndProvincesData[] timeSeriesRecovered;

            for (int i = 0; i < allSlugs.size(); i++) {
                // get time series data for province
                CountryAndProvincesData[][] data = CountryAndProvincesData.getTimeSeriesData(slug);
                timeSeriesConfirmed = data[0];
                // if province match, get case data
                for (int j = 0; j < timeSeriesConfirmed.length; j++) {
                    if (timeSeriesConfirmed[j].getProvince().equals(searchedProvince)) {
                        timeSeriesConfirmedCases.add(timeSeriesConfirmed[j].getCases());
                        confirmedDates.add(timeSeriesConfirmed[j].getDate());
                    }
                }
                timeSeriesDeaths = data[1];
                for (int j = 0; j < timeSeriesDeaths.length; j++) {
                    if (timeSeriesDeaths[j].getProvince().equals(searchedProvince)) {
                        timeSeriesDeathsCases.add(timeSeriesDeaths[j].getCases());
                        deathsDates.add(timeSeriesDeaths[j].getDate());
                    }
                }
                timeSeriesRecovered = data[2];
                for (int j = 0; j < timeSeriesRecovered.length; j++) {
                    if (timeSeriesRecovered[j].getProvince().equals(searchedProvince)) {
                        timeSeriesRecoveredCases.add(timeSeriesRecovered[j].getCases());
                        recoveredDates.add(timeSeriesRecovered[j].getDate());
                    }
                }
            }

            // no province dropdown population (except for UK?)
        }

        // else if searched country is also a province, fetch all data associated with slug
        else if (CountriesAsProvinces.getCountriesAsProvinces().containsKey(searchedCountry)) {
            for (AllCountries country : countriesArray) {
                if (slug.equals(country.getSlug())) {
//                    provinceNames = country.getProvinces();
                }
            }
        }

        // else get case data for slug
        else {

            // get list of provinces to pass in dropdown
            for (AllCountries country : countriesArray) {
                if (slug.equals(country.getSlug())) {
                    provinceNames = country.getProvinces();
                }
            }
        }

        model.addAttribute("countryNames", countryNames);
        model.addAttribute("provinceNames", provinceNames);
        model.addAttribute("searchedProvince", searchedProvince);

        // add case data for province to template
        model.addAttribute("confirmedCases", timeSeriesConfirmedCases);
        model.addAttribute("deathsCases", timeSeriesDeathsCases);
        model.addAttribute("recoveredCases", timeSeriesRecoveredCases);
        model.addAttribute("confirmedDates", confirmedDates);
        model.addAttribute("deathsDates", deathsDates);
        model.addAttribute("recoveredDates", recoveredDates);

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
