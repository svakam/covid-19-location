package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

        Dropdowns.UIFPopData uifPopData = Dropdowns.createCountryDropdownAndUIFPopData(req);
        LinkedList<String> countryDropdown = uifPopData.getDropdown();

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
        CountriesGlobal confData = ApiMethods.getTimeSeriesConf(req);
        // initialize dates
        LinkedList<String> dates = null;
        if (confData != null) {
            // store countries already looked at to avoid duplicating data
            HashSet<String> countriesSeen = new HashSet<>();

            dates = confData.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveTSCaseInfoAPICall(countriesSeen, sc, confData);
            caseInfoForCountry[0] = caseInfo[0];
            caseInfoForCountry[1] = caseInfo[1];
        } else {
            System.out.println("Could not get confirmed series data");
        }

        // get deaths cases for searched country
        CountriesGlobal deathsData = ApiMethods.getTimeSeriesDeaths(req);
        if (deathsData != null) {
            // store countries already looked at to avoid duplicating data
            HashSet<String> countriesSeen = new HashSet<>();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveTSCaseInfoAPICall(countriesSeen, sc, deathsData);
            caseInfoForCountry[2] = caseInfo[0];
            caseInfoForCountry[3] = caseInfo[1];
        } else {
            System.out.println("Could not get confirmed series data");
        }

        // get recovered cases for searched country
        CountriesGlobal recovData = ApiMethods.getTimeSeriesRecov(req);
        if (recovData != null) {
            // store countries already looked at to avoid duplicating data
            HashSet<String> countriesSeen = new HashSet<>();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveTSCaseInfoAPICall(countriesSeen, sc, recovData);
            caseInfoForCountry[4] = caseInfo[0];
            caseInfoForCountry[5] = caseInfo[1];
        }

        // country dropdown
        Dropdowns.UIFPopData uifPopData = Dropdowns.createCountryDropdownAndUIFPopData(req);
        LinkedList<String> countryDropdown = uifPopData.getDropdown();

        // add to template
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

        Dropdowns.UIFPopData uifPopData = Dropdowns.createCountryDropdownAndUIFPopData(req);
        LinkedList<String> countryDropdown = uifPopData.getDropdown();

        model.addAttribute("countryNames", countryDropdown);
        return "provinceResults";
    }

    // error for slug
    @GetMapping("/error/404/{searchedCountry}")
    String error404(Model model, HttpServletRequest req) {

        Dropdowns.UIFPopData uifPopData = Dropdowns.createCountryDropdownAndUIFPopData(req);
        LinkedList<String> countryDropdown = uifPopData.getDropdown();

        model.addAttribute("countryNames", countryDropdown);

        return "error404";
    }

    // fallback
    @GetMapping("*")
    String fallback(Model model, HttpServletRequest req) {

        Dropdowns.UIFPopData uifPopData = Dropdowns.createCountryDropdownAndUIFPopData(req);
        LinkedList<String> countryDropdown = uifPopData.getDropdown();

        model.addAttribute("countryNames", countryDropdown);

        return "error";
    }
}
