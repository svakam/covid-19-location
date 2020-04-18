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
    CountriesGlobal confDataGlobal;
    CountriesGlobal deathsDataGlobal;
    CountriesGlobal recovDataGlobal;

    // uiflookup data

    // hashmap of already looked up countries
    HashMap<String, Integer> lookedUpCountries = new HashMap<>();

    @GetMapping("/")
    String getIndex(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results/country")
    RedirectView submitCountrySearch(String searchedCountry) {

        System.out.println("Dropdown selected = " + searchedCountry);

        return new RedirectView("/results/country?sc=" + searchedCountry);
    }

    @GetMapping("/results/country")
    String resultsForCountry(@RequestParam(required = true, name = "sc") String searchedCountry, Model model, HttpServletRequest req) throws IOException {

        System.out.println("searched country = " + searchedCountry);

        // --------------- set up time series data to pass into template ------------------- //
        // [
        // [confirmed new],
        // [confirmed total],
        // [deaths new],
        // [deaths total],
        // [recov new],
        // [recov total]
        // ]
        @SuppressWarnings("unchecked") LinkedList<Integer>[] caseInfoForCountry = new LinkedList[6]; // for countries without provinces
        // get confirmed cases for searched country
        if (confDataGlobal == null) {
            confDataGlobal = ApiMethods.getTimeSeriesConf(req);
        }
        // initialize dates
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;
        if (confDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            confDates = confDataGlobal.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, confDataGlobal);
            caseInfoForCountry[0] = caseInfo[0];
            caseInfoForCountry[1] = caseInfo[1];
        } else {
            System.out.println("Could not get confirmed series data");
        }
        // get deaths cases for searched country
        if (deathsDataGlobal == null) {
            deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
        }
        if (deathsDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            deathsDates = deathsDataGlobal.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, deathsDataGlobal);
            caseInfoForCountry[2] = caseInfo[0];
            caseInfoForCountry[3] = caseInfo[1];
        } else {
            System.out.println("Could not get confirmed series data");
        }
        // get recovered cases for searched country
        if (recovDataGlobal == null) {
            recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
        }
        if (recovDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            recovDates = recovDataGlobal.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, recovDataGlobal);
            caseInfoForCountry[4] = caseInfo[0];
            caseInfoForCountry[5] = caseInfo[1];
        }

        // ------------ country dropdown and uif pop data -------------- //
        // ------- uif/population data based on searched country ------- //
        UIFMethods.UIFPopData uifPopData = UIFMethods.createCountryDropdownAndUIFPopData(req, searchedCountry);
        LinkedList<String> countryDropdown;
        int uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        if (uifPopData == null) {
            countryDropdown = UIFMethods.createCountryDropdown(req);
        } else {
            countryDropdown = uifPopData.getDropdown();
            uid = uifPopData.getUID();
            iso2 = uifPopData.getIso2();
            iso3 = uifPopData.getIso3();
            code3 = uifPopData.getCode3();
            fips = uifPopData.getFips();
            population = uifPopData.getPopulation();
        }

        // -- get province dropdown based on searched country -- //
        LinkedList<String> provinceDropdown = CountryWithProvinces.getProvincesForCountry(searchedCountry, req);

        // add to template
        if (confDates != null) {
            model.addAttribute("confDates", confDates);
        }
        if (deathsDates != null) {
            model.addAttribute("deathsDates", deathsDates);
        }
        if (recovDates != null) {
            model.addAttribute("recovDates", recovDates);
        }
        model.addAttribute("caseInfoForCountry", caseInfoForCountry);
        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("countryNames", countryDropdown);
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        if (provinceDropdown != null) {
            model.addAttribute("provinceNames", provinceDropdown);
        }
        if (code3 != -1) {
            model.addAttribute("code3", code3);
        }
        if (fips != -1) {
            model.addAttribute("fips", fips);
        }
        model.addAttribute("population", population);

        return "countryResults";
    }

    @PostMapping("/results/country/province")
    RedirectView submitProvinceSearch(String searchedProvince, String countryOfProvince) {
        String rvURL = "/results/country/province?";
        System.out.println("sp = " + searchedProvince);
        System.out.println("cop = " + countryOfProvince);
        rvURL = rvURL + "sp=" + searchedProvince + "&cop=" + countryOfProvince;
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province")
    String resultsForProvince(@RequestParam(required = true, name = "sp") String searchedProvince, @RequestParam(required = true, name = "cop") String countryOfProvince,
                              Model model, HttpServletRequest req) {

        System.out.println("getmapping searched province = " + searchedProvince);
        System.out.println("getmapping searched country = " + countryOfProvince);

        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;

        // ------- get time series data ---------- //
        CountriesGlobal.ProvinceData provinceData = new CountriesGlobal.ProvinceData();
        if (confDataGlobal == null) {
            confDataGlobal = ApiMethods.getTimeSeriesConf(req);
        }
        if (confDataGlobal != null) {
            confDates = confDataGlobal.getDates();
            CountriesGlobal.NewAndConf caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, confDataGlobal);
            if (caseInfo != null) {
                provinceData.setNewConfProvCases(caseInfo.getNewProvCases());
                provinceData.setTotalConfProvCases(caseInfo.getTotalProvCases());
            }
        }
        if (deathsDataGlobal == null) {
            deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
        }
        if (deathsDataGlobal != null) {
            deathsDates = deathsDataGlobal.getDates();
            CountriesGlobal.NewAndConf caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, deathsDataGlobal);
            if (caseInfo != null) {
                provinceData.setNewDeathsProvCases(caseInfo.getNewProvCases());
                provinceData.setTotalDeathsProvCases(caseInfo.getTotalProvCases());
            }
        }
        if (recovDataGlobal == null) {
            recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
        }
        if (recovDataGlobal != null) {
            recovDates = recovDataGlobal.getDates();
            CountriesGlobal.NewAndConf caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, recovDataGlobal);
            if (caseInfo != null) {
                provinceData.setNewRecovProvCases(caseInfo.getNewProvCases());
                provinceData.setTotalRecovProvCases(caseInfo.getTotalProvCases());
            }
        }

        // dropdowns
        UIFMethods.UIFPopData uifPopData = UIFMethods.createCountryDropdownAndUIFPopDataProvince(req, searchedProvince);
        LinkedList<String> countryDropdown;
        int uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        if (uifPopData == null) {
            countryDropdown = UIFMethods.createCountryDropdown(req);
        } else {
            countryDropdown = uifPopData.getDropdown();
            uid = uifPopData.getUID();
            iso2 = uifPopData.getIso2();
            iso3 = uifPopData.getIso3();
            code3 = uifPopData.getCode3();
            fips = uifPopData.getFips();
            population = uifPopData.getPopulation();
        }
        LinkedList<String> provinceDropdown = CountryWithProvinces.getProvincesForCountry(countryOfProvince, req);

        // add to template
        if (confDates != null) {
            model.addAttribute("confDates", confDates);
        }
        if (deathsDates != null) {
            model.addAttribute("deathsDates", deathsDates);
        }
        if (recovDates != null) {
            model.addAttribute("recovDates", recovDates);
        }
        model.addAttribute("countryNames", countryDropdown);
        model.addAttribute("caseInfoForProvince", provinceData);
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        if (provinceDropdown != null) {
            model.addAttribute("provinceNames", provinceDropdown);
        }
        if (code3 != -1) {
            model.addAttribute("code3", code3);
        }
        if (fips != -1) {
            model.addAttribute("fips", fips);
        }
        model.addAttribute("population", population);
        return "provinceResults";
    }

//    @PostMapping("results/country/province/county")
//    RedirectView submitCountySearch(String searchedCounty) {
//        return rv;
//    }

    @GetMapping("/results/country/province/county")
    String resultsForCounty(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "countyResults";
    }

    @GetMapping("/API")
    String APIroutes(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "viewApiRoutes";
    }

    @GetMapping("/tips")
    String getStaySafe(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);
        return "staySafe";
    }

    // fallback
    @GetMapping("*")
    String fallback(Model model, HttpServletRequest req) {

        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req);

        model.addAttribute("countryNames", countryDropdown);

        return "error";
    }
}
