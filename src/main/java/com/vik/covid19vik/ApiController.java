package com.vik.covid19vik;

import com.google.gson.Gson;
import com.sun.tools.javac.util.DefinedBy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

// =============== API calls =============== //
@RestController
class ApiController {
    // consider adding to database:
    CountriesGlobal confDataGlobal;
    CountriesGlobal deathsDataGlobal;
    CountriesGlobal recovDataGlobal;
    USTimeSeries confDataUS;
    USTimeSeries deathsDataUS;
    CountryUIFLookup[] uifData;

    // --------------- global series data ----------------- //
    @GetMapping("API/series/global/confirmed")
    String globalConfirmed() throws IOException {
        String globalConfData = JHUPullMethods.getTimeSeriesGlobalConf();
        if (globalConfData == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return CountriesGlobalDataParse.parseDataToJSON("confirmed", globalConfData);
    }

    @GetMapping("/API/series/global/deaths")
    String globalDeaths() {
        String globalDeathsData = JHUPullMethods.getTimeSeriesGlobalDeaths();
        return CountriesGlobalDataParse.parseDataToJSON("deaths", globalDeathsData);
    }

    @GetMapping("API/series/global/recovered")
    String globalRecovered() {
        String globalRecovData = JHUPullMethods.getTimeSeriesGlobalRecov();
        return CountriesGlobalDataParse.parseDataToJSON("recovered", globalRecovData);
    }

//    // ------------------- US series data ------------------ //
    @GetMapping("API/series/US/confirmed")
    String USConfirmed() {
        String USConfData = JHUPullMethods.getTimeSeriesUSConf();
        return USTimeSeriesParse.parseDataToJSON("confirmed", USConfData);
    }

    @GetMapping("API/series/US/deaths")
    String USDeaths() {
        String USDeathsData = JHUPullMethods.getTimeSeriesUSDeaths();
        return USTimeSeriesParse.parseDataToJSON("deaths", USDeathsData);
    }



//    // ---------------- daily report data ------------------ //
//    @PostMapping("API/daily")
//    void userDateReport(String date) {
//
//    }
//
//    @GetMapping("API/daily")
//    void dailyReport(@RequestParam String date) {
//
//    }

    // ------------ UID, ISO, FIPS lookup data ------------- //
    @GetMapping("API/uifcountries")
    String uifLookup() {
        return CountryUIFLookupParse.parseDatatoJSON();
    }

    // ------------------------- location specific user query --------------------------- //
    @GetMapping("API/query")
    String userQuery(@RequestParam(name = "sc", required = false) String searchedCountry, @RequestParam(name = "sp", required = false) String searchedProvince, @RequestParam(name = "sco", required = false)
                     String searchedCounty, HttpServletRequest req) {
        if (searchedCountry == null && searchedProvince == null && searchedCounty == null) {
            return "No query provided";
        }

        UserQueryLocation location = new UserQueryLocation();

        // if only country was searched for, make API call only to countries global
        if (searchedCountry != null && searchedProvince == null && searchedCounty == null) {
            if (confDataGlobal == null) {
                confDataGlobal = ApiMethods.getTimeSeriesConf(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (confDataGlobal != null) {
                    location.setConfDates(confDataGlobal.getDates());
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, confDataGlobal);
                    location.setNewConf(caseInfo[0]);
                    location.setTotalConf(caseInfo[1]);
                    location.setMostRecentNewConf(caseInfo[2].get(0));
                    location.setMostRecentTotalConf(caseInfo[3].get(0));
                }
            }
            if (deathsDataGlobal == null) {
                deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (deathsDataGlobal != null) {
                    location.setDeathsDates(deathsDataGlobal.getDates());
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, deathsDataGlobal);
                    location.setNewDeaths(caseInfo[0]);
                    location.setTotalDeaths(caseInfo[1]);
                    location.setMostRecentNewDeaths(caseInfo[2].get(0));
                    location.setMostRecentTotalDeaths(caseInfo[3].get(0));
                }
            }
            if (recovDataGlobal == null) {
                recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (recovDataGlobal != null) {
                    location.setRecovDates(recovDataGlobal.getDates());
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, recovDataGlobal);
                    location.setNewRecov(caseInfo[0]);
                    location.setTotalRecov(caseInfo[1]);
                    location.setMostRecentNewRecov(caseInfo[2].get(0));
                    location.setMostRecentTotalRecov(caseInfo[3].get(0));
                }
            }
        } else if (searchedCountry != null) {

        }
        // if searched country is not US or US, get data per query and return as json
        if (!searchedCountry.equals("US") && !searchedCountry.equals("")) {
            if (confDataGlobal == null) {
                confDataGlobal = ApiMethods.getTimeSeriesConf(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (confDataGlobal != null) {
                    confDates = confDataGlobal.getDates();
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, confDataGlobal);
                    location.setNewData(caseInfo[0]);
                    location.setTotalData(caseInfo[1]);
                }
            }
            if (deathsDataGlobal == null) {
                deathsDataGlobal = ApiMethods.getTimeSeriesDeaths(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (deathsDataGlobal != null) {
                    deathsDates = deathsDataGlobal.getDates();
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, deathsDataGlobal);
                }
            }
            if (recovDataGlobal == null) {
                recovDataGlobal = ApiMethods.getTimeSeriesRecov(req);
                HashSet<String> countriesSeen = new HashSet<>();
                if (recovDataGlobal != null) {
                    recovDates = recovDataGlobal.getDates();
                    LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, searchedCountry, recovDataGlobal);
                }
            }

        } else if (searchedCountry.equals("US")) {
            if (confDataUS == null) {
                confDataUS = ApiMethods.getTimeSeriesUSConf(req);
            }
            if (deathsDataUS == null) {
                deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
            }
        } else {
            System.out.println("API query: searched country was null");
        }

    }
}
