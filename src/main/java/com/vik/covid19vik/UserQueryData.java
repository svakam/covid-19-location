package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;

class UserQueryData extends UIFLookup {
    int mostRecentNewConf;
    int mostRecentTotalConf;
    int mostRecentNewDeaths;
    int mostRecentTotalDeaths;
    int mostRecentNewRecov;
    int mostRecentTotalRecov;
    LinkedList<Integer> newConf;
    LinkedList<Integer> totalConf;
    LinkedList<Integer> newDeaths;
    LinkedList<Integer> totalDeaths;
    LinkedList<Integer> newRecov;
    LinkedList<Integer> totalRecov;
    LinkedList<String> confDates;
    LinkedList<String> deathsDates;
    LinkedList<String> recovDates;

    int getMostRecentNewConf() {
        return mostRecentNewConf;
    }
    void setMostRecentNewConf(int mostRecentNewConf) {
        this.mostRecentNewConf = mostRecentNewConf;
    }
    int getMostRecentTotalConf() {
        return mostRecentTotalConf;
    }
    void setMostRecentTotalConf(int mostRecentTotalConf) {
        this.mostRecentTotalConf = mostRecentTotalConf;
    }
    int getMostRecentNewDeaths() {
        return mostRecentNewDeaths;
    }
    void setMostRecentNewDeaths(int mostRecentNewDeaths) {
        this.mostRecentNewDeaths = mostRecentNewDeaths;
    }
    int getMostRecentTotalDeaths() {
        return mostRecentTotalDeaths;
    }
    void setMostRecentTotalDeaths(int mostRecentTotalDeaths) {
        this.mostRecentTotalDeaths = mostRecentTotalDeaths;
    }
    int getMostRecentNewRecov() {
        return mostRecentNewRecov;
    }
    void setMostRecentNewRecov(int mostRecentNewRecov) {
        this.mostRecentNewRecov = mostRecentNewRecov;
    }
    int getMostRecentTotalRecov() {
        return mostRecentTotalRecov;
    }
    void setMostRecentTotalRecov(int mostRecentTotalRecov) {
        this.mostRecentTotalRecov = mostRecentTotalRecov;
    }
    LinkedList<Integer> getNewConf() {
        return newConf;
    }
    void setNewConf(LinkedList<Integer> newConf) {
        this.newConf = newConf;
    }
    LinkedList<Integer> getTotalConf() {
        return totalConf;
    }
    void setTotalConf(LinkedList<Integer> totalConf) {
        this.totalConf = totalConf;
    }
    LinkedList<Integer> getNewDeaths() {
        return newDeaths;
    }
    void setNewDeaths(LinkedList<Integer> newDeaths) {
        this.newDeaths = newDeaths;
    }
    LinkedList<Integer> getTotalDeaths() {
        return totalDeaths;
    }
    void setTotalDeaths(LinkedList<Integer> totalDeaths) {
        this.totalDeaths = totalDeaths;
    }
    LinkedList<Integer> getNewRecov() {
        return newRecov;
    }
    void setNewRecov(LinkedList<Integer> newRecov) {
        this.newRecov = newRecov;
    }
    LinkedList<Integer> getTotalRecov() {
        return totalRecov;
    }
    void setTotalRecov(LinkedList<Integer> totalRecov) {
        this.totalRecov = totalRecov;
    }
    LinkedList<String> getConfDates() {
        return confDates;
    }
    void setConfDates(LinkedList<String> confDates) {
        this.confDates = confDates;
    }
    LinkedList<String> getDeathsDates() {
        return deathsDates;
    }
    void setDeathsDates(LinkedList<String> deathsDates) {
        this.deathsDates = deathsDates;
    }
    LinkedList<String> getRecovDates() {
        return recovDates;
    }
    void setRecovDates(LinkedList<String> recovDates) {
        this.recovDates = recovDates;
    }

    static class UserQuery {
        String searchedCountry;
        String searchedProvince;
        String searchedCounty;

        UserQuery(String searchedCountry, String searchedProvince, String searchedCounty) {
            this.searchedCountry = searchedCountry;
            this.searchedProvince = searchedProvince;
            this.searchedCounty = searchedCounty;
        }
    }

    static UserQueryData getGlobalData(JHUTimeSeriesAndUIFData data, UserQuery userQuery, UserQueryData location, HttpServletRequest req) {
        CountriesGlobal confDataGlobal = data.getConfDataGlobal();
        CountriesGlobal deathsDataGlobal = data.getDeathsDataGlobal();
        CountriesGlobal recovDataGlobal = data.getRecovDataGlobal();

        // confirmed data
        if (confDataGlobal == null) {
            data.setConfDataGlobal(ApiMethods.getTimeSeriesConf(req));
        }
        if (confDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            location.setConfDates(data.getConfDataGlobal().getDates());
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, userQuery.searchedCountry, confDataGlobal);
            location.setNewConf(caseInfo[0]);
            location.setTotalConf(caseInfo[1]);
            location.setMostRecentNewConf(caseInfo[2].get(0));
            location.setMostRecentTotalConf(caseInfo[3].get(0));
        }

        // deaths data
        if (data.getDeathsDataGlobal() == null) {
            data.setDeathsDataGlobal(ApiMethods.getTimeSeriesDeaths(req));
        }
        if (deathsDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            location.setDeathsDates(deathsDataGlobal.getDates());
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, userQuery.searchedCountry, deathsDataGlobal);
            location.setNewDeaths(caseInfo[0]);
            location.setTotalDeaths(caseInfo[1]);
            location.setMostRecentNewDeaths(caseInfo[2].get(0));
            location.setMostRecentTotalDeaths(caseInfo[3].get(0));
        }

        // recovered data
        if (data.getRecovDataGlobal() == null) {
            data.setRecovDataGlobal(ApiMethods.getTimeSeriesRecov(req));
        }
        if (recovDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            location.setRecovDates(recovDataGlobal.getDates());
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, userQuery.searchedCountry, recovDataGlobal);
            location.setNewRecov(caseInfo[0]);
            location.setTotalRecov(caseInfo[1]);
            location.setMostRecentNewRecov(caseInfo[2].get(0));
            location.setMostRecentTotalRecov(caseInfo[3].get(0));
        }

        return location;
    }

    static UserQueryData getUSData() {

    }

    static UserQueryData getUIFData() {

    }

    static UserQueryData getData(JHUTimeSeriesAndUIFData data, UserQuery userQuery, HttpServletRequest req) {

        UserQueryData location = new UserQueryData();
        String searchedCountry = userQuery.searchedCountry;
        String searchedProvince = userQuery.searchedProvince;
        String searchedCounty = userQuery.searchedCounty;

        // only searched province as query
        if (searchedCountry == null && searchedProvince != null && searchedCounty == null) {
            // if searched province is not a US province

                // hit global data
                location = getGlobalData(data, userQuery, location, req);
                // hit UIFP data
                location = getUIFData();
            // else
                // hit US data
        }

        // all are query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty != null) {
            // hit global data for US country data
            // hit US data for province and county data
        }

        // searched country and province as query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty == null) {
            // if searched country not US
                // hit global data
                // hit UIFP data
            // else
                // hit US data
        }

        // query only contains searched country
        else if (searchedCountry != null && searchedProvince == null && searchedCounty == null) {
            // hit global data
            // hit UIFP data
        }

        // only searched county as query
        else if (searchedCountry == null && searchedProvince == null && searchedCounty != null) {
            // hit US data
        }

        // searched province and county as query
        else if (searchedCountry == null && searchedProvince != null && searchedCounty != null) {
            // hit US data
        }

        // searched country and county as query
        else if (searchedCountry != null && searchedProvince == null && searchedCounty != null) {
            // hit US data
        }
    }
}
