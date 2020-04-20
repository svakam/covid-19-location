package com.vik.covid19vik;

import com.sun.tools.javac.util.DefinedBy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

// ================ render templates ================= //
@Controller
class MainController {

    // consider adding to database:
    // time series:
    // country global data,
    CountriesGlobal confDataGlobal;
    CountriesGlobal deathsDataGlobal;
    CountriesGlobal recovDataGlobal;
    // US data
    USTimeSeries confDataUS;
    USTimeSeries deathsDataUS;

    // uiflookup data
    CountryUIFLookup[] countries;

    // hashmap of already looked up countries
    HashMap<String, Integer> lookedUpCountries = new HashMap<>();

    // ============================================================================== //
    // =================================  home ====================================== //
    // ============================================================================== //
    @GetMapping("/")
    String getIndex(Model model, HttpServletRequest req) {

        if (countries == null) {
           countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req, countries);

        model.addAttribute("countryNames", countryDropdown);
        return "index";
    }

    // ============================================================================== //
    // ============================  country results ================================ //
    // ============================================================================== //

    // post request with user's country name search
    @PostMapping("/results/country")
    RedirectView submitCountrySearch(String searchedCountry) {

        System.out.println("Dropdown selected = " + searchedCountry);

        return new RedirectView("/results/country?sc=" + searchedCountry);
    }

    @GetMapping("/results/country")
    String resultsForCountry(@RequestParam(required = true, name = "sc") String searchedCountry, Model model, HttpServletRequest req) throws IOException, ParseException {

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

        // get confirmed cases for searched country
        if (confDataGlobal == null) {
            confDataGlobal = ApiMethods.getTimeSeriesConf(req);
        }
        // initialize dates
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;

        // initialize graph data
        Map<Object, Object>[] graphNewConf;
        Map<Object, Object>[] graphTotalConf;
        Map<Object, Object>[] graphNewDeaths;
        Map<Object, Object>[] graphTotalDeaths;
        Map<Object, Object>[] graphNewRecovs;
        Map<Object, Object>[] graphTotalRecovs;

        // initialize other
        int uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;

        if (confDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            confDates = confDataGlobal.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, confDataGlobal);
            graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
            graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
            model.addAttribute("graphNewConf", graphNewConf);
            model.addAttribute("graphTotalConf", graphTotalConf);
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
            graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[0]);
            graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[1]);
            model.addAttribute("graphNewDeaths", graphNewDeaths);
            model.addAttribute("graphTotalDeaths", graphTotalDeaths);
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
            graphNewRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[0]);
            graphTotalRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[1]);
            model.addAttribute("graphNewRecovs", graphNewRecovs);
            model.addAttribute("graphTotalRecovs", graphTotalRecovs);
        }


        // ------------ country dropdown and uif pop data -------------- //
        // ------- uif/population data based on searched country ------- //
        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        UIFMethods.UIFPopData uifPopData = UIFMethods.createCountryDropdownAndUIFPopData(req, searchedCountry, countries);
        LinkedList<String> countryDropdown;
        if (uifPopData == null) {
            countryDropdown = UIFMethods.createCountryDropdown(req, countries);
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
        LinkedList<String> provinceDropdown = CountryWithProvinces.getProvincesForCountry(searchedCountry, countries);

        // add to template
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



    // ============================================================================== //
    // ===========================  province results ================================ //
    // ============================================================================== //

    @PostMapping("/results/country/province")
    RedirectView submitProvinceSearch(String searchedProvince, String countryOfProvince) {
        String rvURL = "/results/country/province?";
        System.out.println("sp = " + searchedProvince);
        System.out.println("cop = " + countryOfProvince);
        rvURL = rvURL + "sp=" + searchedProvince + "&cop=" + countryOfProvince;
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province")
    String resultsForProvince(@RequestParam(name = "sp") String searchedProvince, @RequestParam(name = "cop") String countryOfProvince,
                              Model model, HttpServletRequest req) {

        System.out.println("getmapping searched province = " + searchedProvince);
        System.out.println("getmapping searched country = " + countryOfProvince);

        // initializers:
        // both US and non-US
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;
        LinkedList<String> provinceDropdown;
        @SuppressWarnings("unchecked") LinkedList<Integer>[] provinceData;
        int uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        // non-US
        // US
        UIFMethods.UIFPopData uifPopData;
        LinkedList<String> countryDropdown;

        // ---------- get province data and store in province data object ----------- //
        // non-US data
        if (!countryOfProvince.equals("US")) {
            // ------- get time series data ---------- //
            provinceData = new LinkedList[6];
            if (confDataGlobal == null) {
                confDataGlobal = ApiMethods.getTimeSeriesConf(req);
            } else {
                confDates = confDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, confDataGlobal);
                if (caseInfo != null) {
                    provinceData[0] = caseInfo[0];
                    provinceData[1] = caseInfo[1];
                }
            }
            if (deathsDataGlobal == null) {
                deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
            } else {
                deathsDates = deathsDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, deathsDataGlobal);
                if (caseInfo != null) {
                    provinceData[2] = caseInfo[0];
                    provinceData[3] = caseInfo[1];
                }
            }
            if (recovDataGlobal == null) {
                recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
            } else {
                recovDates = recovDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, recovDataGlobal);
                if (caseInfo != null) {
                    provinceData[4] = caseInfo[0];
                    provinceData[5] = caseInfo[1];
                }
            }
        }
        // US data
        else {
            // add all county data up for searched state for each confirmed and deaths data and add to list of state data
            // use searched state to retrieve data from API call results
            provinceData = new LinkedList[4];
            if (confDataUS == null) {
                confDataUS = ApiMethods.getTimeSeriesUSConf(req);
            } else {
                confDates = confDataUS.getDates();
                LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(searchedProvince, confDataUS);
                if (caseInfo != null) {
                    provinceData[0] = caseInfo[0];
                    provinceData[1] = caseInfo[1];
                }
            }
            if (deathsDataUS == null) {
                deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
            } else {
                deathsDates = deathsDataUS.getDates();
                LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(searchedProvince, deathsDataUS);
                if (caseInfo != null) {
                    provinceData[2] = caseInfo[0];
                    provinceData[3] = caseInfo[1];
                }
            }
        }

        // dropdowns
        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        uifPopData = UIFMethods.createCountryDropdownAndUIFPopDataProvince(searchedProvince, countries);
        if (uifPopData != null) {
            countryDropdown = uifPopData.getDropdown();
            uid = uifPopData.getUID();
            iso2 = uifPopData.getIso2();
            iso3 = uifPopData.getIso3();
            code3 = uifPopData.getCode3();
            fips = uifPopData.getFips();
            population = uifPopData.getPopulation();
        }
        else {
            countryDropdown = UIFMethods.createCountryDropdown(req, countries);
        }
        // province dropdown
        provinceDropdown = CountryWithProvinces.getProvincesForCountry(countryOfProvince, countries);


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
        model.addAttribute("countryOfProvince", countryOfProvince);
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

    // ============================================================================== //
    // ============================  county results ================================ //
    // ============================================================================== //

//    @PostMapping("results/country/province/county")
//    RedirectView submitCountySearch(String searchedCounty, String provinceOfCounty, String countryOfProvince, Model model, HttpServletRequest req) {
//        return rv;
//    }

    @GetMapping("/results/country/province/county")
    String resultsForCounty(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req, countries);

        model.addAttribute("countryNames", countryDropdown);
        return "countyResults";
    }


    // ============================================================================== //
    // ============================= show API routes ================================ //
    // ============================================================================== //
    @GetMapping("/API")
    String APIroutes(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req, countries);

        model.addAttribute("countryNames", countryDropdown);
        return "viewApiRoutes";
    }

    // ============================================================================== //
    // ==============================  safety tips ================================== //
    // ============================================================================== //
    @GetMapping("/tips")
    String getStaySafe(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req, countries);

        model.addAttribute("countryNames", countryDropdown);
        return "staySafe";
    }

    // =================================== fallback ================================== //
    @GetMapping("*")
    String fallback(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(req, countries);

        model.addAttribute("countryNames", countryDropdown);

        return "error";
    }
}
