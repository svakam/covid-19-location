package com.vik.covid19vik;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

// =============== API calls =============== //
@RestController
class ApiController {
    // consider adding to database:
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
}
