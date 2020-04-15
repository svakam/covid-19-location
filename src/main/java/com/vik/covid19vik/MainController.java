package com.vik.covid19vik;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import sun.awt.image.ImageWatched;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

// ================ render templates ================= //
@Controller
class MainController {

    // consider adding to database:
    // country global data
    // uiflookup data

    // hashmap of already looked up countries
    HashMap<String, Integer> lookedUpCountries = new HashMap<>();

    @GetMapping("/")
    String getIndex(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = Dropdowns.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results/country")
    RedirectView submitCountrySearch(String searchedCountry) {

        System.out.println("Dropdown selected = " + searchedCountry);

        return new RedirectView("/results/country?sc=" + searchedCountry);
    }

//    @PostMapping("/results/country/province")
//    public RedirectView submitProvinceSearch(String searchedProvince, String countryForProvince) {
//        return rv;
//    }

    @GetMapping("/results/country")
    String resultsForCountry(@RequestParam(required = true, name = "sc") String sc, Model model, HttpServletRequest req) throws IOException {

        System.out.println("searched country = " + sc);

        // set up time series data to pass in
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForCountry = new LinkedList[6]; // for countries without provinces
        // [
        // [confirmed new],
        // [confirmed total],
        // [deaths new],
        // [deaths total],
        // [recov new],
        // [recov total]
        // ]

        // get confirmed cases for searched country
        CountryGlobal confData = ApiMethods.getTimeSeriesConf(req);
        // initialize dates
        LinkedList<String> dates = null;
        if (confData != null) {
            // store countries already looked at to avoid duplicating data
            HashSet<String> countriesSeen = new HashSet<>();

            dates = confData.getDates();
            LinkedList<CountryGlobal.Country> countries = confData.getCountries();
            for (int i = 0; i < countries.size(); i++) {
                CountryGlobal.Country country = countries.get(i);
                if (!countriesSeen.contains(country.getCountryOrRegion())) {
                    // if a country has no provinces associated with it, it has the full time series data associated with it; just retrieve data normally
                    if (country.getCountryOrRegion().equals(sc) && country.getProvinceOrState().equals("")) {
                        LinkedList<Integer> newConfCases = country.getNewCases();
                        caseInfoForCountry[0] = newConfCases;
                        LinkedList<Integer> totalConfCases = country.getTotalCases();
                        caseInfoForCountry[1] = totalConfCases;
                        countriesSeen.add(country.getCountryOrRegion());
                    }
                    // if a country has provinces associated with it, add up each province data date by date to get country's total
                    else if (country.getCountryOrRegion().equals(sc) && !country.getProvinceOrState().equals("")) {
                        // make sure there isn't an object associated with the country further downstream that doesn't have a province
                        // as long as the country is still the current country, do this check
                        LinkedList<Integer> allNewConfCases = new LinkedList<>();
                        LinkedList<Integer> allTotalConfCases = new LinkedList<>();
                        int j = 0;
                        int restOfCountries = countries.size() - i;
                        while (j < restOfCountries) {
                            if (countries.get(j).getCountryOrRegion().equals(country.getCountryOrRegion()) && countries.get(j).getProvinceOrState().equals("")) {
                                LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                                caseInfoForCountry[0] = newConfCases;
                                LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                                caseInfoForCountry[1] = totalConfCases;
                                break;
                            } else {
                                // if province exists, add case data to final array and go to next country
                                LinkedList<Integer> newConfCases = countries.get(j).getNewCases();
                                if (allNewConfCases.size() == 0) {
                                    allNewConfCases.addAll(newConfCases);
                                } else {
                                    // iterate through total list and add current province's cases
                                    for (int caseIndex = 0; caseIndex < allNewConfCases.size(); caseIndex++) {
                                        // get cases at index of total cases, add province cases to those cases, and update at index of total cases
                                        allNewConfCases.set(caseIndex, (newConfCases.get(caseIndex) + allNewConfCases.get(caseIndex)));
                                    }
                                }
                                LinkedList<Integer> totalConfCases = countries.get(j).getTotalCases();
                                if (allTotalConfCases.size() == 0) {
                                    allTotalConfCases.addAll(totalConfCases);
                                } else {
                                    // iterate through total list and add current province's cases
                                    for (int caseIndex = 0; caseIndex < allTotalConfCases.size(); caseIndex++) {
                                        // get cases at index of total cases, add province cases to those cases, and update at index of total cases
                                        allTotalConfCases.set(caseIndex, (totalConfCases.get(caseIndex) + allTotalConfCases.get(caseIndex)));
                                    }
                                }
                                j++;
                            }
                        }
                        caseInfoForCountry[0] = allNewConfCases;
                        caseInfoForCountry[1] = allTotalConfCases;
                        countriesSeen.add(country.getCountryOrRegion());
                    }
                }
            }
        } else {
            System.out.println("Could not get confirmed series data");
        }

        // get deaths cases for searched country
        CountryGlobal deathsData = ApiMethods.getTimeSeriesDeaths(req);
        if (deathsData != null) {
            LinkedList<CountryGlobal.Country> countries = deathsData.getCountries();
            for (CountryGlobal.Country country : countries) {
                if (country.getCountryOrRegion().equals(sc) && country.getProvinceOrState().equals("")) {
                    LinkedList<Integer> newDeathsCases = country.getNewCases();
                    caseInfoForCountry[2] = newDeathsCases;
                    LinkedList<Integer> totalDeathsCases = country.getTotalCases();
                    caseInfoForCountry[3] = totalDeathsCases;
                }
            }
        }
        else {
            System.out.println("could not get deaths series data");
        }

        // get recovered cases for searched country
        CountryGlobal recovData = ApiMethods.getTimeSeriesRecov(req);
        if (recovData != null) {
            LinkedList<CountryGlobal.Country> countries = recovData.getCountries();
            for (CountryGlobal.Country country : countries) {
                if (country.getCountryOrRegion().equals(sc) && country.getProvinceOrState().equals("")) {
                    LinkedList<Integer> newRecovCases = country.getNewCases();
                    caseInfoForCountry[4] = newRecovCases;
                    LinkedList<Integer> totalRecovCases = country.getTotalCases();
                    caseInfoForCountry[5] = totalRecovCases;
                }
            }
        }

        // country dropdown
        LinkedList<String> countryDropdown = Dropdowns.createCountryDropdown(req);

        if (dates != null) {
            model.addAttribute("dates", dates);
        }
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);
        model.addAttribute("searchedCountry", sc);
        model.addAttribute("countryNames", countryDropdown);
        return "countryResults";
    }

    @GetMapping("/results/country/province")
    String resultsForProvince(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = Dropdowns.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "provinceResults";
    }

    // error for slug
    @GetMapping("/error/404/{searchedCountry}")
    String error404(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = Dropdowns.createCountryDropdown(req);
        model.addAttribute("countryNames", countryDropdown);

        return "error404";
    }

    // fallback
    @GetMapping("*")
    String fallback(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = Dropdowns.createCountryDropdown(req);
        model.addAttribute("countryNames", countryDropdown);

        return "error";
    }
}
