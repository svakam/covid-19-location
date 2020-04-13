package com.vik.covid19vik;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// =============== API calls =============== //
@RestController
class ApiController {
    // --------------- global series data ----------------- //
    @GetMapping("/API/series/global/confirmed")
    CountryGlobal[] globalConfirmed() {
        String globalConfData = JHUPullMethods.getTimeSeriesGlobalConf();
        CountryGlobal[] confirmedJson = CountryGlobalDataParse.fromJSON("confirmed", globalConfData);
        return confirmedJson;
    }

    @GetMapping("/API/series/global/deaths")
    CountryGlobal[] globalDeaths() {
        String globalDeathsData = JHUPullMethods.getTimeSeriesGlobalDeaths();
        CountryGlobal[] deathsJson = CountryGlobalDataParse.fromJSON("deaths", globalDeathsData);
        return deathsJson;
    }

    @GetMapping("API/series/global/recovered")
    CountryGlobal[] globalRecovered() {
        String globalRecovData = JHUPullMethods.getTimeSeriesGlobalRecov();
        CountryGlobal[] recovJson = CountryGlobalDataParse.fromJSON("recovered", globalRecovData);
        return recovJson;
    }

//    // ------------------- US series data ------------------ //
//    @GetMapping("API/series/US/confirmed")
//    void USConfirmed() {
//
//    }
//
//    @GetMapping("API/series/US/confirmed")
//    void USDeaths() {
//
//    }
//
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
    CountryUIFLookup[] uifLookup() {
        CountryUIFLookup[] uifCountries = CountryUIFLookupParse.fromJSON();
        return uifCountries;
    }
}
