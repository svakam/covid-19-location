package com.vik.covid19vik;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// =============== API calls =============== //
@RestController
class ApiController {
    // consider adding to database:
    // --------------- global series data ----------------- //
    @GetMapping("API/series/global/confirmed")
    String globalConfirmed() {
        String globalConfData = JHUPullMethods.getTimeSeriesGlobalConf();
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
    void USConfirmed() {
        String USConfData = JHUPullMethods.getTimeSeriesUSConf();

    }

    @GetMapping("API/series/US/deaths")
    void USDeaths() {
        String USDeathsData = JHUPullMethods.getTimeSeriesUSDeaths();

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

    // ------------ UID, IOS, FIPS lookup data ------------- //
    @GetMapping("API/uifcountries")
    String uifLookup() {
        return CountryUIFLookupParse.parseDatatoJSON();
    }
}
