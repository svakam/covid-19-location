package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;

class UserQueryLocation extends UIFLookup {
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

    static UserQueryLocation getData(JHUTimeSeriesAndUIFData data, HttpServletRequest req) {

        UserQueryLocation location = new UserQueryLocation();

        if (data.getConfDataGlobal() == null) {
            data.setConfDataGlobal(ApiMethods.getTimeSeriesConf(req));
            HashSet<String> countriesSeen = new HashSet<>();
            if (data.getConfDataGlobal() != null) {
                location.setConfDates(data.getConfDataGlobal().getDates());
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
}
