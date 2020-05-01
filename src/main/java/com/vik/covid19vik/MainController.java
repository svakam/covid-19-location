package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);

        model.addAttribute("countryNames", countryDropdown);
        return "index";
    }

    // ============================================================================== //
    // ============================  country results ================================ //
    // ============================================================================== //

    // post request with user's country name search
    @PostMapping("/results/country")
    RedirectView submitCountrySearch(String searchedCountry) throws UnsupportedEncodingException {

//        System.out.println("Dropdown selected = " + searchedCountry);

        return new RedirectView("/results/country?sc=" + URLEncoder.encode(searchedCountry, "UTF-8"));
    }

    @GetMapping("/results/country")
    String resultsForCountry(@RequestParam(name = "sc") String searchedCountry, @RequestParam(required = false, name = "endpoint") String endpoint,
                             @RequestParam(required = false, name = "checks") List<String> checks, Model model, HttpServletRequest req) throws IOException, ParseException {

        // if number valid, show success message, else failure message
        if (endpoint != null) {
            model.addAttribute("endpoint", endpoint);
        }
        if (checks != null) {
            model.addAttribute("checks", checks);
        }
//        System.out.println("searched country = " + searchedCountry);

        // --------------- set up time series data to pass into template ------------------- //
        // [
        // [confirmed new],
        // [confirmed total],
        // [deaths new],
        // [deaths total],
        // [recov new],
        // [recov total]
        // ]

        // initialize dates
        LinkedList<String> confDates;
        LinkedList<String> deathsDates;
        LinkedList<String> recovDates;

        // initialize graph data
        Map<Object, Object>[] graphNewConf;
        Map<Object, Object>[] graphTotalConf;
        Map<Object, Object>[] graphNewDeaths;
        Map<Object, Object>[] graphTotalDeaths;
        Map<Object, Object>[] graphNewRecovs;
        Map<Object, Object>[] graphTotalRecovs;

        // initialize table data
        @SuppressWarnings("unchecked") LinkedList<Integer>[] countryDataTable = new LinkedList[6];

        // initialize other
        float uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        String combinedKey = null;

        // retrieve data
        if (confDataGlobal == null) {
            confDataGlobal = ApiMethods.getTimeSeriesConf(req);
        }
        if (confDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            confDates = confDataGlobal.getDates();
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, confDataGlobal);
            graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
            graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
            int recentNewConf = caseInfo[2].get(0);
            int recentTotalConf = caseInfo[3].get(0);
            countryDataTable[0] = caseInfo[0];
            countryDataTable[1] = caseInfo[1];
            model.addAttribute("graphNewConf", graphNewConf);
            model.addAttribute("graphTotalConf", graphTotalConf);
            model.addAttribute("recentNewConf", recentNewConf);
            model.addAttribute("recentTotalConf", recentTotalConf);
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
            int recentNewDeaths = caseInfo[2].get(0);
            int recentTotalDeaths = caseInfo[3].get(0);
            countryDataTable[2] = caseInfo[0];
            countryDataTable[3] = caseInfo[1];
            model.addAttribute("graphNewDeaths", graphNewDeaths);
            model.addAttribute("graphTotalDeaths", graphTotalDeaths);
            model.addAttribute("recentNewDeaths", recentNewDeaths);
            model.addAttribute("recentTotalDeaths", recentTotalDeaths);
        } else {
            System.out.println("Could not get deaths series data");
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
            int recentNewRecov = caseInfo[2].get(0);
            int recentTotalRecov = caseInfo[3].get(0);
            countryDataTable[4] = caseInfo[0];
            countryDataTable[5] = caseInfo[1];
            model.addAttribute("graphNewRecovs", graphNewRecovs);
            model.addAttribute("graphTotalRecovs", graphTotalRecovs);
            model.addAttribute("recentNewRecov", recentNewRecov);
            model.addAttribute("recentTotalRecov", recentTotalRecov);
        } else {
            System.out.println("Could not get recovered series data");
        }


        // ------------ country dropdown and uif pop data -------------- //
        // ------- uif/population data based on searched country ------- //
        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        UIFMethods.UIFPopData uifPopData = UIFMethods.createCountryDropdownAndUIFPopData(req, searchedCountry, countries);
        LinkedList<String> countryDropdown;
        if (uifPopData == null) {
            countryDropdown = UIFMethods.createCountryDropdown(countries);
        } else {
            countryDropdown = uifPopData.getDropdown();
            uid = uifPopData.getUID();
            iso2 = uifPopData.getIso2();
            iso3 = uifPopData.getIso3();
            code3 = uifPopData.getCode3();
            fips = uifPopData.getFips();
            population = uifPopData.getPopulation();
            combinedKey = uifPopData.getCombinedKey();
        }

        // -- get province dropdown based on searched country -- //
        LinkedList<String> provinceDropdown = CountryWithProvinces.getProvincesForCountry(searchedCountry, countries);

        // add to template
        model.addAttribute("countryDataTable", countryDataTable);
        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("countryNames", countryDropdown);
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        model.addAttribute("combinedKey", combinedKey);
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
        model.addAttribute("currentURL", req.getRequestURL() + "?" + req.getQueryString());
        return "results";
    }



    // ============================================================================== //
    // ===========================  province results ================================ //
    // ============================================================================== //

    @PostMapping("/results/country/province")
    RedirectView submitProvinceSearch(String searchedProvince, String searchedCountry) throws UnsupportedEncodingException {
//        System.out.println("sp = " + searchedProvince);
//        System.out.println("sc = " + searchedCountry);
        String rvURL = "/results/country/province?";
        rvURL = rvURL + "sc=" + URLEncoder.encode(searchedCountry, "UTF-8") + "&sp=" + URLEncoder.encode(searchedProvince, "UTF-8");
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province")
    String resultsForProvince(@RequestParam(name = "sc") String searchedCountry, @RequestParam(name = "sp") String searchedProvince,
                               @RequestParam(name = "endpoint", required = false) String endpoint, @RequestParam(name = "checks", required = false) List<String> checks,
                              Model model, HttpServletRequest req) throws ParseException {

        // if number valid, show success message, else failure message
        if (endpoint != null) {
            model.addAttribute("endpoint", endpoint);
        }
        if (checks != null) {
            model.addAttribute("checks", checks);
        }

//        System.out.println("getmapping searched province = " + searchedProvince);
//        System.out.println("getmapping searched country = " + searchedCountry);

        // initializers:
        // both US and non-US
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;
        LinkedList<String> provinceDropdown;
        LinkedList<String> countyDropdown;
        @SuppressWarnings("unchecked") LinkedList<Integer>[] provinceDataTable;
        float uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        String combinedKey = "";
        // non-US
        // US
        UIFMethods.UIFPopData uifPopData;
        LinkedList<String> countryDropdown;

        // initialize graph data
        Map<Object, Object>[] graphNewConf;
        Map<Object, Object>[] graphTotalConf;
        Map<Object, Object>[] graphNewDeaths;
        Map<Object, Object>[] graphTotalDeaths;
        Map<Object, Object>[] graphNewRecovs;
        Map<Object, Object>[] graphTotalRecovs;

        // ---------- get province data and store in province data object ----------- //
        // non-US data
        if (!searchedCountry.equals("US")) {
            // ------- get time series data ---------- //
            provinceDataTable = new LinkedList[6];
            if (confDataGlobal == null) {
                confDataGlobal = ApiMethods.getTimeSeriesConf(req);
            }
            if (confDataGlobal != null) {
                confDates = confDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, confDataGlobal);
                if (caseInfo != null) {
                    provinceDataTable[0] = caseInfo[0];
                    provinceDataTable[1] = caseInfo[1];
                    int recentNewConf = caseInfo[2].get(0);
                    int recentTotalConf = caseInfo[3].get(0);
                    graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
                    graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
                    model.addAttribute("graphNewConf", graphNewConf);
                    model.addAttribute("graphTotalConf", graphTotalConf);
                    model.addAttribute("recentNewConf", recentNewConf);
                    model.addAttribute("recentTotalConf", recentTotalConf);
                }
            }
            if (deathsDataGlobal == null) {
                deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
            }
            if (deathsDataGlobal != null) {
                deathsDates = deathsDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, deathsDataGlobal);
                if (caseInfo != null) {
                    provinceDataTable[2] = caseInfo[0];
                    provinceDataTable[3] = caseInfo[1];
                    int recentNewDeaths = caseInfo[2].get(0);
                    int recentTotalDeaths = caseInfo[3].get(0);
                    graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[0]);
                    graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[1]);
                    model.addAttribute("graphNewDeaths", graphNewDeaths);
                    model.addAttribute("graphTotalDeaths", graphTotalDeaths);
                    model.addAttribute("recentNewDeaths", recentNewDeaths);
                    model.addAttribute("recentTotalDeaths", recentTotalDeaths);
                }
            }
            if (recovDataGlobal == null) {
                recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
            }
            if (recovDataGlobal != null){
                recovDates = recovDataGlobal.getDates();
                LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveProvinceTSInfoAPICall(searchedProvince, recovDataGlobal);
                if (caseInfo != null) {
                    provinceDataTable[4] = caseInfo[0];
                    provinceDataTable[5] = caseInfo[1];
                    int recentNewRecov = caseInfo[2].get(0);
                    int recentTotalRecov = caseInfo[3].get(0);
                    graphNewRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[0]);
                    graphTotalRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[1]);
                    model.addAttribute("graphNewRecovs", graphNewRecovs);
                    model.addAttribute("graphTotalRecovs", graphTotalRecovs);
                    model.addAttribute("recentNewRecov", recentNewRecov);
                    model.addAttribute("recentTotalRecov", recentTotalRecov);
                }
            }
        }

        // US data
        else {
            // add all county data up for searched state for each confirmed and deaths data and add to list of state data
            // use searched state to retrieve data from API call results
            provinceDataTable = new LinkedList[4];
            if (confDataUS == null) {
                confDataUS = ApiMethods.getTimeSeriesUSConf(req);
            }
            if (confDataUS != null) {
                // create county dropdown
                countyDropdown = USTimeSeries.createCountyDropdown(searchedProvince, confDataUS);
                if (countyDropdown != null && countyDropdown.size() > 0) {
                    model.addAttribute("countyNames", countyDropdown);
                }

                // get data
                confDates = confDataUS.getDates();
                LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(searchedProvince, confDataUS);
                if (caseInfo != null) {
                    provinceDataTable[0] = caseInfo[0];
                    provinceDataTable[1] = caseInfo[1];
                    int recentNewConf = caseInfo[2].get(0);
                    int recentTotalConf = caseInfo[3].get(0);
                    graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
                    graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
                    if (graphNewConf.length > 0) {
                        model.addAttribute("graphNewConf", graphNewConf);
                    }
                    if(graphTotalConf.length > 0) {
                        model.addAttribute("graphTotalConf", graphTotalConf);
                    }
                    model.addAttribute("recentNewConf", recentNewConf);
                    model.addAttribute("recentTotalConf", recentTotalConf);
                }
            }
            if (deathsDataUS == null) {
                deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
            }
            if (deathsDataUS != null) {
                deathsDates = deathsDataUS.getDates();
                LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(searchedProvince, deathsDataUS);
                if (caseInfo != null) {
                    provinceDataTable[2] = caseInfo[0];
                    provinceDataTable[3] = caseInfo[1];
                    int recentNewDeaths = caseInfo[2].get(0);
                    int recentTotalDeaths = caseInfo[3].get(0);
                    graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[0]);
                    graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[1]);
                    if (graphNewDeaths.length > 0) {
                        model.addAttribute("graphNewDeaths", graphNewDeaths);
                    }
                    if (graphTotalDeaths.length > 0) {
                        model.addAttribute("graphTotalDeaths", graphTotalDeaths);
                    }
                    model.addAttribute("recentNewDeaths", recentNewDeaths);
                    model.addAttribute("recentTotalDeaths", recentTotalDeaths);
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
            combinedKey = uifPopData.getCombinedKey();
        }
        else {
            countryDropdown = UIFMethods.createCountryDropdown(countries);
        }


        // province dropdown
        provinceDropdown = CountryWithProvinces.getProvincesForCountry(searchedCountry, countries);



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
        model.addAttribute("searchedProvince", searchedProvince);
        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("countryNames", countryDropdown);
        model.addAttribute("provinceDataTable", provinceDataTable);
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        model.addAttribute("combinedKey", combinedKey);
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
        model.addAttribute("currentURL", req.getRequestURL() + "?" + req.getQueryString());
        return "results";
    }

    // ============================================================================== //
    // ============================  county results ================================ //
    // ============================================================================== //

    @PostMapping("results/country/province/county")
    RedirectView submitCountySearch(String searchedCounty, String searchedCountry, String searchedProvince) throws UnsupportedEncodingException {
//        System.out.println("searched country = " + searchedCountry);
//        System.out.println("searched province = " + searchedProvince);
//        System.out.println("searched county = " + searchedCounty);
        String rvURL = "/results/country/province/county?";
        rvURL = rvURL + "sc=" + URLEncoder.encode(searchedCountry, "UTF-8") + "&sp=" + URLEncoder.encode(searchedProvince, "UTF-8") + "&sco=" + URLEncoder.encode(searchedCounty, "UTF-8");
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province/county")
    String resultsForCounty(@RequestParam(name = "sc") String searchedCountry, @RequestParam(name = "sp") String searchedProvince,
                            @RequestParam(name = "sco") String searchedCounty, @RequestParam(required = false, name = "endpoint") String endpoint,
                            @RequestParam(required = false, name = "checks") List<String> checks, HttpServletRequest req, Model model) throws ParseException {

//        System.out.println("getmapping searched province = " + searchedProvince);
//        System.out.println("getmapping searched country = " + searchedCountry);
//        System.out.println("getmapping searched county = " + searchedCounty);

        // if number valid, show success message, else failure message
        if (endpoint != null) {
            model.addAttribute("endpoint", endpoint);
        }
        if (checks != null) {
            model.addAttribute("checks", checks);
        }

        // initializers
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> provinceDropdown;
        LinkedList<String> countyDropdown = null;
        USTimeSeries.CountyCaseAndUIF countyPull = null;
        @SuppressWarnings("unchecked") LinkedList<Integer>[] countyDataTable = new LinkedList[4];
        float uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;
        String combinedKey = "";

        if (confDataUS == null) {
            confDataUS = ApiMethods.getTimeSeriesUSConf(req);
        }
        if (confDataUS != null) {
            // create county dropdown
            countyDropdown = USTimeSeries.createCountyDropdown(searchedProvince, confDataUS);

            // get data
            confDates = confDataUS.getDates();
            countyPull = USTimeSeries.retrieveCountyTSInfoAPICall(searchedCounty, searchedProvince, confDataUS);
            if (countyPull != null) {
                countyDataTable[0] = countyPull.getSumNewCasesAcrossCounty();
                countyDataTable[1] = countyPull.getSumTotalCasesAcrossCounty();
                int recentNewConf = countyPull.getRecentNewData();
                int recentTotalConf = countyPull.getRecentTotalData();
                Map<Object, Object>[] graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, countyPull.getSumNewCasesAcrossCounty());
                Map<Object, Object>[] graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, countyPull.getSumTotalCasesAcrossCounty());
                if (graphNewConf.length > 0) {
                    model.addAttribute("graphNewConf", graphNewConf);
                }
                if (graphTotalConf.length > 0) {
                    model.addAttribute("graphTotalConf", graphTotalConf);
                }
                model.addAttribute("recentNewConf", recentNewConf);
                model.addAttribute("recentTotalConf", recentTotalConf);
            }
        }
        if (deathsDataUS == null) {
            deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
        }
        if (deathsDataUS != null) {
            deathsDates = deathsDataUS.getDates();
            countyPull = USTimeSeries.retrieveCountyTSInfoAPICall(searchedCounty, searchedProvince, deathsDataUS);
            if (countyPull != null) {
                countyDataTable[2] = countyPull.getSumNewCasesAcrossCounty();
                countyDataTable[3] = countyPull.getSumTotalCasesAcrossCounty();
                int recentNewDeaths = countyPull.getRecentNewData();
                int recentTotalDeaths = countyPull.getRecentTotalData();
                Map<Object, Object>[] graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumNewCasesAcrossCounty());
                Map<Object, Object>[] graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumTotalCasesAcrossCounty());
                if (graphNewDeaths.length > 0) {
                    model.addAttribute("graphNewDeaths", graphNewDeaths);
                }
                if (graphTotalDeaths.length > 0) {
                    model.addAttribute("graphTotalDeaths", graphTotalDeaths);
                }
                model.addAttribute("recentNewDeaths", recentNewDeaths);
                model.addAttribute("recentTotalDeaths", recentTotalDeaths);
            }
        }

        // uif data
        if (countyPull != null) {
            uid = countyPull.getUid();
            iso2 = countyPull.getIso2();
            iso3 = countyPull.getIso3();
            code3 = countyPull.getCode3();
            fips = countyPull.getFips();
            population = countyPull.getPopulation();
            combinedKey = countyPull.getCombinedKey();
        }

        // country dropdown
        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);
        // province dropdown
        provinceDropdown = CountryWithProvinces.getProvincesForCountry(searchedCountry, countries);

        // add to template
        model.addAttribute("searchedCountry", searchedCountry);
        model.addAttribute("searchedProvince", searchedProvince);
        model.addAttribute("searchedCounty", searchedCounty);
        model.addAttribute("countryNames", countryDropdown);
        model.addAttribute("provinceNames", provinceDropdown);
        if (countyDropdown != null && countyDropdown.size() > 0) {
            model.addAttribute("countyNames", countyDropdown);
        }
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        model.addAttribute("code3", code3);
        model.addAttribute("fips", fips);
        model.addAttribute("population", population);
        model.addAttribute("combinedKey", combinedKey);
        model.addAttribute("countyDataTable", countyDataTable);
        model.addAttribute("currentURL", req.getRequestURL() + "?" + req.getQueryString());
        return "results";
    }



    // ============================================================================== //
    // ==================================== SNS ===================================== //
    // ============================================================================== //
    @PostMapping("/sms/subscribe")
    RedirectView subscribeSNS(String snsAdd, String currentURL, String countryCheck, String provinceCheck, String countyCheck) throws UnsupportedEncodingException {
        currentURL = AWSSNSMethods.checkURLRedundant(currentURL);
        currentURL = AWSDynamoDBMethods.checkURLRedundant(currentURL);

        // if nothing was selected, return a failure message
        if (countryCheck == null && provinceCheck == null && countyCheck == null) {
            return new RedirectView(currentURL + "&checks=empty");
        }

        // add to URL anything selected
        StringBuilder checks = new StringBuilder();
        if (countryCheck != null) {
            checks.append(URLEncoder.encode(countryCheck, "UTF-8"));
        }
        if (checks.length() == 0 && provinceCheck != null) {
            checks.append(URLEncoder.encode(provinceCheck, "UTF-8"));
        } else if (checks.length() > 0 && provinceCheck != null) {
            checks.append(',').append(URLEncoder.encode(provinceCheck, "UTF-8"));
        }
        if (checks.length() == 0 && countyCheck != null) {
            checks.append(URLEncoder.encode(countyCheck, "UTF-8"));
        } else if (checks.length() > 0 && countyCheck != null) {
            checks.append(',').append(URLEncoder.encode(countyCheck, "UTF-8"));
        }
        currentURL = currentURL + "&checks=" + checks.toString();

        // validate endpoint
        String regexComplete = "\\+1\\d{10}";
        String regexNoCountryCode = "\\d{10}";
        Pattern compiledComplete = Pattern.compile(regexComplete);
        Pattern compiledNoCountrycode = Pattern.compile(regexNoCountryCode);
        Matcher matcherComplete = compiledComplete.matcher(snsAdd);
        Matcher matcherNoCC = compiledNoCountrycode.matcher(snsAdd);
        String endpoint;
        if (matcherComplete.matches()) {
            endpoint = "valid";
        } else if (matcherNoCC.matches()) {
            snsAdd = "+1" + snsAdd;
            endpoint = "valid";
        }
        // else don't subscribe/publish
        else {
            endpoint = "invalid";
        }

        // DynamoDB: check if endpoint exists in DB; if in DB, update with new fields, else create new item
        // SNS: subscribe and publish confirmation message to endpoint
        if (endpoint.equals("valid")) {
            AWSDynamoDBMethods.addRequestToDB(snsAdd, countryCheck, provinceCheck, countyCheck);
            AWSSNSMethods.subscribeEndpoint(snsAdd, countryCheck, provinceCheck, countyCheck);
        }

        return new RedirectView(currentURL + "&endpoint=" + endpoint);
    }

    @PostMapping("/sms/unsubscribe")
    RedirectView unsubscribeSNS(String snsRemove, String currentURL) {

        return new RedirectView(currentURL);
    }


    // ============================================================================== //
    // ============================= show API routes ================================ //
    // ============================================================================== //
    @GetMapping("/api")
    String APIroutes(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);

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
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);

        model.addAttribute("countryNames", countryDropdown);
        return "staySafe";
    }

    // =================================== fallback ================================== //
    @GetMapping("*")
    String fallback(Model model, HttpServletRequest req) {

        if (countries == null) {
            countries = ApiMethods.getUIFLookup(req);
        }
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);

        model.addAttribute("countryNames", countryDropdown);

        return "error";
    }
}
