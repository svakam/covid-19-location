package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;

class UserQueryData extends UIFLookup {
    // country
    int countryMostRecentNewConf;
    int countryMostRecentTotalConf;
    int countryMostRecentNewDeaths;
    int countryMostRecentTotalDeaths;
    int countryMostRecentNewRecov;
    int countryMostRecentTotalRecov;
    LinkedList<Integer> countryNewConf;
    LinkedList<Integer> countryTotalConf;
    LinkedList<Integer> countryNewDeaths;
    LinkedList<Integer> countryTotalDeaths;
    LinkedList<Integer> countryNewRecov;
    LinkedList<Integer> countryTotalRecov;
    // province
    int provinceMostRecentNewConf;
    int provinceMostRecentTotalConf;
    int provinceMostRecentNewDeaths;
    int provinceMostRecentTotalDeaths;
    int provinceMostRecentNewRecov;
    int provinceMostRecentTotalRecov;
    LinkedList<Integer> provinceNewConf;
    LinkedList<Integer> provinceTotalConf;
    LinkedList<Integer> provinceNewDeaths;
    LinkedList<Integer> provinceTotalDeaths;
    LinkedList<Integer> provinceNewRecov;
    LinkedList<Integer> provinceTotalRecov;
    // county
    int countyMostRecentNewConf;
    int countyMostRecentTotalConf;
    int countyMostRecentNewDeaths;
    int countyMostRecentTotalDeaths;
    int countyMostRecentNewRecov;
    int countyMostRecentTotalRecov;
    LinkedList<Integer> countyNewConf;
    LinkedList<Integer> countyTotalConf;
    LinkedList<Integer> countyNewDeaths;
    LinkedList<Integer> countyTotalDeaths;
    LinkedList<Integer> countyNewRecov;
    LinkedList<Integer> countyTotalRecov;
    // dates
    LinkedList<String> confDates;
    LinkedList<String> deathsDates;
    LinkedList<String> recovDates;

    int getCountryMostRecentNewConf() {
        return countryMostRecentNewConf;
    }
    void setCountryMostRecentNewConf(int countryMostRecentNewConf) {
        this.countryMostRecentNewConf = countryMostRecentNewConf;
    }
    int getCountryMostRecentTotalConf() {
        return countryMostRecentTotalConf;
    }
    void setCountryMostRecentTotalConf(int countryMostRecentTotalConf) {
        this.countryMostRecentTotalConf = countryMostRecentTotalConf;
    }
    int getCountryMostRecentNewDeaths() {
        return countryMostRecentNewDeaths;
    }
    void setCountryMostRecentNewDeaths(int countryMostRecentNewDeaths) {
        this.countryMostRecentNewDeaths = countryMostRecentNewDeaths;
    }
    int getCountryMostRecentTotalDeaths() {
        return countryMostRecentTotalDeaths;
    }
    void setCountryMostRecentTotalDeaths(int countryMostRecentTotalDeaths) {
        this.countryMostRecentTotalDeaths = countryMostRecentTotalDeaths;
    }
    int getCountryMostRecentNewRecov() {
        return countryMostRecentNewRecov;
    }
    void setCountryMostRecentNewRecov(int countryMostRecentNewRecov) {
        this.countryMostRecentNewRecov = countryMostRecentNewRecov;
    }
    int getCountryMostRecentTotalRecov() {
        return countryMostRecentTotalRecov;
    }
    void setCountryMostRecentTotalRecov(int countryMostRecentTotalRecov) {
        this.countryMostRecentTotalRecov = countryMostRecentTotalRecov;
    }
    LinkedList<Integer> getCountryNewConf() {
        return countryNewConf;
    }
    void setCountryNewConf(LinkedList<Integer> countryNewConf) {
        this.countryNewConf = countryNewConf;
    }
    LinkedList<Integer> getCountryTotalConf() {
        return countryTotalConf;
    }
    void setCountryTotalConf(LinkedList<Integer> countryTotalConf) {
        this.countryTotalConf = countryTotalConf;
    }
    LinkedList<Integer> getCountryNewDeaths() {
        return countryNewDeaths;
    }
    void setCountryNewDeaths(LinkedList<Integer> countryNewDeaths) {
        this.countryNewDeaths = countryNewDeaths;
    }
    LinkedList<Integer> getCountryTotalDeaths() {
        return countryTotalDeaths;
    }
    void setCountryTotalDeaths(LinkedList<Integer> countryTotalDeaths) {
        this.countryTotalDeaths = countryTotalDeaths;
    }
    LinkedList<Integer> getCountryNewRecov() {
        return countryNewRecov;
    }
    void setCountryNewRecov(LinkedList<Integer> countryNewRecov) {
        this.countryNewRecov = countryNewRecov;
    }
    LinkedList<Integer> getCountryTotalRecov() {
        return countryTotalRecov;
    }
    void setCountryTotalRecov(LinkedList<Integer> countryTotalRecov) {
        this.countryTotalRecov = countryTotalRecov;
    }

    int getProvinceMostRecentNewConf() {
        return provinceMostRecentNewConf;
    }
    void setProvinceMostRecentNewConf(int provinceMostRecentNewConf) {
        this.provinceMostRecentNewConf = provinceMostRecentNewConf;
    }
    int getProvinceMostRecentTotalConf() {
        return provinceMostRecentTotalConf;
    }
    void setProvinceMostRecentTotalConf(int provinceMostRecentTotalConf) {
        this.provinceMostRecentTotalConf = provinceMostRecentTotalConf;
    }
    int getProvinceMostRecentNewDeaths() {
        return provinceMostRecentNewDeaths;
    }
    void setProvinceMostRecentNewDeaths(int provinceMostRecentNewDeaths) {
        this.provinceMostRecentNewDeaths = provinceMostRecentNewDeaths;
    }
    int getProvinceMostRecentTotalDeaths() {
        return provinceMostRecentTotalDeaths;
    }
    void setProvinceMostRecentTotalDeaths(int provinceMostRecentTotalDeaths) {
        this.provinceMostRecentTotalDeaths = provinceMostRecentTotalDeaths;
    }
    int getProvinceMostRecentNewRecov() {
        return provinceMostRecentNewRecov;
    }
    void setProvinceMostRecentNewRecov(int provinceMostRecentNewRecov) {
        this.provinceMostRecentNewRecov = provinceMostRecentNewRecov;
    }
    int getProvinceMostRecentTotalRecov() {
        return provinceMostRecentTotalRecov;
    }
    void setProvinceMostRecentTotalRecov(int provinceMostRecentTotalRecov) {
        this.provinceMostRecentTotalRecov = provinceMostRecentTotalRecov;
    }
    LinkedList<Integer> getProvinceNewConf() {
        return provinceNewConf;
    }
    void setProvinceNewConf(LinkedList<Integer> provinceNewConf) {
        this.provinceNewConf = provinceNewConf;
    }
    LinkedList<Integer> getProvinceTotalConf() {
        return provinceTotalConf;
    }
    void setProvinceTotalConf(LinkedList<Integer> provinceTotalConf) {
        this.provinceTotalConf = provinceTotalConf;
    }
    LinkedList<Integer> getProvinceNewDeaths() {
        return provinceNewDeaths;
    }
    void setProvinceNewDeaths(LinkedList<Integer> provinceNewDeaths) {
        this.provinceNewDeaths = provinceNewDeaths;
    }
    LinkedList<Integer> getProvinceTotalDeaths() {
        return provinceTotalDeaths;
    }
    void setProvinceTotalDeaths(LinkedList<Integer> provinceTotalDeaths) {
        this.provinceTotalDeaths = provinceTotalDeaths;
    }
    LinkedList<Integer> getProvinceNewRecov() {
        return provinceNewRecov;
    }
    void setProvinceNewRecov(LinkedList<Integer> provinceNewRecov) {
        this.provinceNewRecov = provinceNewRecov;
    }
    LinkedList<Integer> getProvinceTotalRecov() {
        return provinceTotalRecov;
    }
    void setProvinceTotalRecov(LinkedList<Integer> provinceTotalRecov) {
        this.provinceTotalRecov = provinceTotalRecov;
    }

    int getCountyMostRecentNewConf() {
        return countyMostRecentNewConf;
    }
    void setCountyMostRecentNewConf(int countyMostRecentNewConf) {
        this.countyMostRecentNewConf = countyMostRecentNewConf;
    }
    int getCountyMostRecentTotalConf() {
        return countyMostRecentTotalConf;
    }
    void setCountyMostRecentTotalConf(int countyMostRecentTotalConf) {
        this.countyMostRecentTotalConf = countyMostRecentTotalConf;
    }
    int getCountyMostRecentNewDeaths() {
        return countyMostRecentNewDeaths;
    }
    void setCountyMostRecentNewDeaths(int countyMostRecentNewDeaths) {
        this.countyMostRecentNewDeaths = countyMostRecentNewDeaths;
    }
    int getCountyMostRecentTotalDeaths() {
        return countyMostRecentTotalDeaths;
    }
    void setCountyMostRecentTotalDeaths(int countyMostRecentTotalDeaths) {
        this.countyMostRecentTotalDeaths = countyMostRecentTotalDeaths;
    }
    int getCountyMostRecentNewRecov() {
        return countyMostRecentNewRecov;
    }
    void setCountyMostRecentNewRecov(int countyMostRecentNewRecov) {
        this.countyMostRecentNewRecov = countyMostRecentNewRecov;
    }
    int getCountyMostRecentTotalRecov() {
        return countyMostRecentTotalRecov;
    }
    void setCountyMostRecentTotalRecov(int countyMostRecentTotalRecov) {
        this.countyMostRecentTotalRecov = countyMostRecentTotalRecov;
    }
    LinkedList<Integer> getCountyNewConf() {
        return countyNewConf;
    }
    void setCountyNewConf(LinkedList<Integer> countyNewConf) {
        this.countyNewConf = countyNewConf;
    }
    LinkedList<Integer> getCountyTotalConf() {
        return countyTotalConf;
    }
    void setCountyTotalConf(LinkedList<Integer> countyTotalConf) {
        this.countyTotalConf = countyTotalConf;
    }
    LinkedList<Integer> getCountyNewDeaths() {
        return countyNewDeaths;
    }
    void setCountyNewDeaths(LinkedList<Integer> countyNewDeaths) {
        this.countyNewDeaths = countyNewDeaths;
    }
    LinkedList<Integer> getCountyTotalDeaths() {
        return countyTotalDeaths;
    }
    void setCountyTotalDeaths(LinkedList<Integer> countyTotalDeaths) {
        this.countyTotalDeaths = countyTotalDeaths;
    }
    LinkedList<Integer> getCountyNewRecov() {
        return countyNewRecov;
    }
    void setCountyNewRecov(LinkedList<Integer> countyNewRecov) {
        this.countyNewRecov = countyNewRecov;
    }
    LinkedList<Integer> getCountyTotalRecov() {
        return countyTotalRecov;
    }
    void setCountyTotalRecov(LinkedList<Integer> countyTotalRecov) {
        this.countyTotalRecov = countyTotalRecov;
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
}
