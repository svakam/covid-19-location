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
        LinkedList<String> countryDropdown = UIFMethods.createCountryDropdown(countries);

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

        // initialize table data
        @SuppressWarnings("unchecked") LinkedList<Integer>[] countryData = new LinkedList[6];

        // initialize other
        int uid = -1;
        String iso2 = null;
        String iso3 = null;
        int code3 = -1;
        int fips = -1;
        int population = -1;

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
            countryData[0] = caseInfo[0];
            countryData[1] = caseInfo[1];
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
            countryData[2] = caseInfo[0];
            countryData[3] = caseInfo[1];
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
            countryData[4] = caseInfo[0];
            countryData[5] = caseInfo[1];
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
            countryDropdown = UIFMethods.createCountryDropdown(countries);
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
        model.addAttribute("countryData", countryData);
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
    RedirectView submitProvinceSearch(String searchedProvince, String searchedCountry) {
        System.out.println("sp = " + searchedProvince);
        System.out.println("sc = " + searchedCountry);
        String rvURL = "/results/country/province?";
        rvURL = rvURL + "sc=" + searchedCountry + "&sp=" + searchedProvince;
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province")
    String resultsForProvince(@RequestParam(name = "sc") String searchedCountry, @RequestParam(name = "sp") String searchedProvince,
                               Model model, HttpServletRequest req) throws ParseException {

        System.out.println("getmapping searched province = " + searchedProvince);
        System.out.println("getmapping searched country = " + searchedCountry);

        // initializers:
        // both US and non-US
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> recovDates = null;
        LinkedList<String> provinceDropdown;
        LinkedList<String> countyDropdown;
        @SuppressWarnings("unchecked") LinkedList<Integer>[] provinceDataTable;
        int uid = -1;
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
                    graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
                    graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
                    model.addAttribute("graphNewConf", graphNewConf);
                    model.addAttribute("graphTotalConf", graphTotalConf);
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
                    graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[0]);
                    graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[1]);
                    model.addAttribute("graphNewDeaths", graphNewDeaths);
                    model.addAttribute("graphTotalDeaths", graphTotalDeaths);
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
                    graphNewRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[0]);
                    graphTotalRecovs = CanvasJSChartData.convertToXYPoints(recovDates, caseInfo[1]);
                    model.addAttribute("graphNewRecovs", graphNewRecovs);
                    model.addAttribute("graphTotalRecovs", graphTotalRecovs);
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
                    graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[0]);
                    graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, caseInfo[1]);
                    model.addAttribute("graphNewConf", graphNewConf);
                    model.addAttribute("graphTotalConf", graphTotalConf);
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
                    graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[0]);
                    graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, caseInfo[1]);
                    model.addAttribute("graphNewDeaths", graphNewDeaths);
                    model.addAttribute("graphTotalDeaths", graphTotalDeaths);
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
        return "provinceResults";
    }

    // ============================================================================== //
    // ============================  county results ================================ //
    // ============================================================================== //

    @PostMapping("results/country/province/county")
    RedirectView submitCountySearch(String searchedCounty, String searchedCountry, String searchedProvince) {
        System.out.println("searched country = " + searchedCountry);
        System.out.println("searched province = " + searchedProvince);
        System.out.println("searched county = " + searchedCounty);
        String rvURL = "/results/country/province/county?";
        rvURL = rvURL + "sc=" + searchedCountry + "&sp=" + searchedProvince + "&sco=" + searchedCounty;
        return new RedirectView(rvURL);
    }

    @GetMapping("/results/country/province/county")
    String resultsForCounty(@RequestParam(name = "sc") String searchedCountry, @RequestParam(name = "sp") String searchedProvince,
                            @RequestParam(name = "sco") String searchedCounty, HttpServletRequest req, Model model) throws ParseException {

        System.out.println("getmapping searched province = " + searchedProvince);
        System.out.println("getmapping searched country = " + searchedCountry);
        System.out.println("getmapping searched county = " + searchedCounty);

        // initializers
        LinkedList<String> confDates = null;
        LinkedList<String> deathsDates = null;
        LinkedList<String> provinceDropdown;
        LinkedList<String> countyDropdown = null;
        USTimeSeries.CountyCaseAndUIF countyPull = null;
        @SuppressWarnings("unchecked") LinkedList<Integer>[] countyDataTable = new LinkedList[4];
        int uid = -1;
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
            model.addAttribute("countyNames", countyDropdown);

            // get data
            confDates = confDataUS.getDates();
            countyPull = USTimeSeries.retrieveCountyTSInfoAPICall(searchedCounty, searchedProvince, confDataUS);
            if (countyPull != null) {
                countyDataTable[0] = countyPull.getSumNewCasesAcrossCounty();
                countyDataTable[1] = countyPull.getSumTotalCasesAcrossCounty();
                Map<Object, Object>[] graphNewConf = CanvasJSChartData.convertToXYPoints(confDates, countyPull.getSumNewCasesAcrossCounty());
                Map<Object, Object>[] graphTotalConf = CanvasJSChartData.convertToXYPoints(confDates, countyPull.getSumTotalCasesAcrossCounty());
                model.addAttribute("graphNewConf", graphNewConf);
                model.addAttribute("graphTotalConf", graphTotalConf);
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
                Map<Object, Object>[] graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumNewCasesAcrossCounty());
                Map<Object, Object>[] graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumTotalCasesAcrossCounty());
                model.addAttribute("graphNewDeaths", graphNewDeaths);
                model.addAttribute("graphTotalDeaths", graphTotalDeaths);
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
        model.addAttribute("countyNames", countyDropdown);
        model.addAttribute("uid", uid);
        model.addAttribute("iso2", iso2);
        model.addAttribute("iso3", iso3);
        model.addAttribute("code3", code3);
        model.addAttribute("fips", fips);
        model.addAttribute("population", population);
        model.addAttribute("combinedKey", combinedKey);
        model.addAttribute("countyDataTable", countyDataTable);
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
