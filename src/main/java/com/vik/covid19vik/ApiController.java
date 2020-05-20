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
        String globalConfDataCSV = JHUPullMethods.getTimeSeriesGlobalConf();
        if (globalConfDataCSV == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return CountriesGlobalDataParse.parseDataToJSON("confirmed", globalConfDataCSV);
    }

    @GetMapping("/API/series/global/deaths")
    String globalDeaths() throws IOException {
        String globalDeathsDataCSV = JHUPullMethods.getTimeSeriesGlobalDeaths();
        if (globalDeathsDataCSV == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return CountriesGlobalDataParse.parseDataToJSON("deaths", globalDeathsDataCSV);
    }

    @GetMapping("API/series/global/recovered")
    String globalRecovered() throws IOException {
        String globalRecovDataCSV = JHUPullMethods.getTimeSeriesGlobalRecov();
        if (globalRecovDataCSV == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return CountriesGlobalDataParse.parseDataToJSON("recovered", globalRecovDataCSV);
    }

//    // ------------------- US series data ------------------ //
    @GetMapping("API/series/US/confirmed")
    String USConfirmed() throws IOException {
        String USConfDataCSV = JHUPullMethods.getTimeSeriesUSConf();
        if (USConfDataCSV == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return USTimeSeriesParse.parseDataToJSON("confirmed", USConfDataCSV);
    }

    @GetMapping("API/series/US/deaths")
    String USDeaths() throws IOException {
        String USDeathsDataCSV = JHUPullMethods.getTimeSeriesUSDeaths();
        if (USDeathsDataCSV == null) {
            System.out.println("Could not pull from JHU CSSE");
            throw new IOException("Error 500");
        }
        return USTimeSeriesParse.parseDataToJSON("deaths", USDeathsDataCSV);
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
    String userQuery(@RequestParam(name = "sc", required = false) String searchedCountry, @RequestParam(name = "sp", required = false) String searchedProvince,
                     @RequestParam(name = "sco", required = false) String searchedCounty, HttpServletRequest req) {

        UserQueryLocation location = new UserQueryLocation();

        // no params
        if (searchedCountry == null && searchedProvince == null && searchedCounty == null) {
            return "No query provided";
        }

        // only searched province as query
        else if (searchedCountry == null && searchedProvince != null && searchedCounty == null) {

        }

        // all are query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty != null) {

        }

        // searched country and province as query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty == null) {

        }

        // query only contains searched country
        else if (searchedCountry != null && searchedProvince == null && searchedCounty == null) {
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
        }

        // only searched county as query
        else if (searchedCountry == null && searchedProvince == null && searchedCounty != null) {

        }

        // searched province and county as query
        else if (searchedCountry == null && searchedProvince != null && searchedCounty != null) {

        }

        // searched country and county as query
        else if (searchedCountry != null && searchedProvince == null && searchedCounty != null) {

        }

        // return json
        Gson gson = new Gson();

    }
}
