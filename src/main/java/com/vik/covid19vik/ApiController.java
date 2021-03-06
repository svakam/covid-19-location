package com.vik.covid19vik;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// =============== API calls =============== //
@RestController
class ApiController {

    // needs to be in database or server cache
    JHUTimeSeriesAndUIFData jhuTimeSeriesAndUIFData = new JHUTimeSeriesAndUIFData();

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
        return UIFLookupParse.parseDatatoJSON();
    }

    // ------------------------- location specific user query --------------------------- //
    @GetMapping("API/query")
    String userQuery(@RequestParam(name = "sc", required = false) String searchedCountry, @RequestParam(name = "sp", required = false) String searchedProvince,
                     @RequestParam(name = "sco", required = false) String searchedCounty, HttpServletRequest req) {

        // no params
        if (searchedCountry == null && searchedProvince == null && searchedCounty == null) {
            return "No query provided";
        }
        // multiple counties of same name are possible
        if (searchedCountry.equals("US") && searchedProvince == null && searchedCounty != null) {
            return "Ambiguous US query - must include a state";
        }

        UserQueryData.UserQuery userQuery = new UserQueryData.UserQuery(searchedCountry, searchedProvince, searchedCounty);
        UserQueryData locationData = UseryQueryDataMethods.getData(jhuTimeSeriesAndUIFData, userQuery, req);

        // return json
        Gson gson = new Gson();
        return gson.toJson(locationData);
    }
}
