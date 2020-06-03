package com.vik.covid19vik;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class UseryQueryDataMethods {
    static UserQueryData getData(JHUTimeSeriesAndUIFData data, UserQueryData.UserQuery userQuery, HttpServletRequest req) {

        UserQueryData location = new UserQueryData();
        String searchedCountry = userQuery.searchedCountry;
        String searchedProvince = userQuery.searchedProvince;
        String searchedCounty = userQuery.searchedCounty;

        // only searched province as query
        if (searchedCountry == null && searchedProvince != null && searchedCounty == null) {
            // if searched province is not a US province
            if () {
                // hit global data
                getGlobalData(data, userQuery, location, req);
                // hit UIFP data
                getUIFData(data, userQuery, location, req);
            }
            // else hit US data
            else {
                getUSDataState(data, userQuery, location, req);
            }
        }

        // all are query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty != null) {
            // hit global data for US country data
            getGlobalData(data, userQuery, location, req);
            // hit US data for province and county data
            getUSDataState(data, userQuery, location, req);
            getUSDataCounty(data, userQuery, location, req);
        }

        // searched country and province as query
        else if (searchedCountry != null && searchedProvince != null && searchedCounty == null) {
            // hit global data for country
            getGlobalData(data, userQuery, location, req);

            // if searched country not US
            if (!searchedCountry.equals("US")) {
                // hit UIFP data
                getUIFData(data, userQuery, location, req);
            }
            // else hit US data
            else {
                getUSDataState(data, userQuery, location, req);

            }
        }

        // query only contains searched country
        else if (searchedCountry != null && searchedProvince == null && searchedCounty == null) {
            // hit global data
            getGlobalData(data, userQuery, location, req);
            // hit UIFP data
            getUIFData(data, userQuery, location, req);
        }

        // only searched county as query
        else if (searchedCountry == null && searchedProvince == null && searchedCounty != null) {
            // hit US county data
            getUSDataCounty(data, userQuery, location, req);
        }

        // searched province and county as query
        else if (searchedCountry == null && searchedProvince != null && searchedCounty != null) {
            // hit US data state and county
            getUSDataState(data, userQuery, location, req);
            getUSDataCounty(data, userQuery, location, req);
        }

        return location;
    }

    static void getGlobalData(JHUTimeSeriesAndUIFData data, UserQueryData.UserQuery userQuery, UserQueryData location, HttpServletRequest req) {
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
            location.setCountryNewConf(caseInfo[0]);
            location.setCountryTotalConf(caseInfo[1]);
            location.setCountryMostRecentNewConf(caseInfo[2].get(0));
            location.setCountryMostRecentTotalConf(caseInfo[3].get(0));
        } else {
            System.out.println("Could not get confirmed series data");
        }

        // deaths data
        if (data.getDeathsDataGlobal() == null) {
            data.setDeathsDataGlobal(ApiMethods.getTimeSeriesDeaths(req));
        }
        if (deathsDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            location.setDeathsDates(deathsDataGlobal.getDates());
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, userQuery.searchedCountry, deathsDataGlobal);
            location.setCountryNewDeaths(caseInfo[0]);
            location.setCountryTotalDeaths(caseInfo[1]);
            location.setCountryMostRecentNewDeaths(caseInfo[2].get(0));
            location.setCountryMostRecentTotalDeaths(caseInfo[3].get(0));
        } else {
            System.out.println("Could not get deaths series data");
        }

        // recovered data
        if (data.getRecovDataGlobal() == null) {
            data.setRecovDataGlobal(ApiMethods.getTimeSeriesRecov(req));
        }
        if (recovDataGlobal != null) {
            HashSet<String> countriesSeen = new HashSet<>();
            location.setRecovDates(recovDataGlobal.getDates());
            LinkedList<Integer>[] caseInfo = CountriesGlobal.retrieveCountryTSInfoAPICall(countriesSeen, userQuery.searchedCountry, recovDataGlobal);
            location.setCountryNewRecov(caseInfo[0]);
            location.setCountryTotalRecov(caseInfo[1]);
            location.setCountryMostRecentNewRecov(caseInfo[2].get(0));
            location.setCountryMostRecentTotalRecov(caseInfo[3].get(0));
        } else {
            System.out.println("Could not get recovered series data");
        }
    }

    static void getUSDataState(JHUTimeSeriesAndUIFData data, UserQueryData.UserQuery userQuery, UserQueryData location, HttpServletRequest req) {
        USTimeSeries confDataUS = data.getConfDataUS();
        USTimeSeries deathsDataUS = data.getDeathsDataUS();

        if (confDataUS == null) {
            confDataUS = ApiMethods.getTimeSeriesUSConf(req);
        }
        if (confDataUS != null) {
            location.setConfDates(confDataUS.getDates());
            LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(userQuery.searchedProvince, confDataUS);
            if (caseInfo != null) {
                location.setProvinceNewConf(caseInfo[0]);
                location.setProvinceTotalConf(caseInfo[1]);
                location.setProvinceMostRecentNewConf(caseInfo[2].get(0));
                location.setProvinceMostRecentTotalConf(caseInfo[3].get(0));
            }
        }
        if (deathsDataUS == null) {
            deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
        }
        if (deathsDataUS != null) {
            location.setDeathsDates(deathsDataUS.getDates());
            LinkedList<Integer>[] caseInfo = USTimeSeries.retrieveProvinceTSInfoAPICall(userQuery.searchedProvince, deathsDataUS);
            if (caseInfo != null) {
                location.setProvinceNewDeaths(caseInfo[0]);
                location.setProvinceTotalDeaths(caseInfo[1]);
                location.setProvinceMostRecentNewDeaths(caseInfo[2].get(0));
                location.setProvinceMostRecentTotalDeaths(caseInfo[3].get(0));
            }
        }
    }

    static void getUSDataCounty(JHUTimeSeriesAndUIFData data, UserQueryData.UserQuery userQuery, UserQueryData location, HttpServletRequest req) {
        USTimeSeries confDataUS = data.getConfDataUS();
        USTimeSeries deathsDataUS = data.getDeathsDataUS();
        USTimeSeries.CountyCaseAndUIF countyPull = null;

        if (confDataUS == null) {
            confDataUS = ApiMethods.getTimeSeriesUSConf(req);
        }
        if (confDataUS != null) {

            // get data
            location.setConfDates(confDataUS.getDates());
            countyPull = USTimeSeries.retrieveCountyTSInfoAPICall(userQuery.searchedCounty, userQuery.searchedProvince, confDataUS);
            if (countyPull != null) {
                location.setCountyMostRecentNewConf(countyPull.getRecentNewData());
                location.setCountyMostRecentTotalConf(countyPull.getRecentTotalData());
                location.setCountyNewConf(countyPull.getSumNewCasesAcrossCounty());
                location.setCountyTotalConf(countyPull.getSumTotalCasesAcrossCounty());
            }
        }
        if (deathsDataUS == null) {
            deathsDataUS = ApiMethods.getTimeSeriesUSDeaths(req);
        }
        if (deathsDataUS != null) {
            location.setDeathsDates(deathsDataUS.getDates());
            countyPull = USTimeSeries.retrieveCountyTSInfoAPICall(userQuery.searchedCounty, userQuery.searchedProvince, deathsDataUS);
            if (countyPull != null) {
                countyDataTable[2] = countyPull.getSumNewCasesAcrossCounty();
                countyDataTable[3] = countyPull.getSumTotalCasesAcrossCounty();
                int recentNewDeaths = countyPull.getRecentNewData();
                int recentTotalDeaths = countyPull.getRecentTotalData();
                Map<Object, Object>[] graphNewDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumNewCasesAcrossCounty());
                Map<Object, Object>[] graphTotalDeaths = CanvasJSChartData.convertToXYPoints(deathsDates, countyPull.getSumTotalCasesAcrossCounty());
                if (graphNewDeaths.length > 0) {
                    model.addAttribute("graphNewDeaths", graphNewDeaths);
                }
                if (graphTotalDeaths.length > 0) {
                    model.addAttribute("graphTotalDeaths", graphTotalDeaths);
                }
                model.addAttribute("recentNewDeaths", recentNewDeaths);
                model.addAttribute("recentTotalDeaths", recentTotalDeaths);
            }
        }
    }

    static void getUIFData(JHUTimeSeriesAndUIFData data, UserQueryData.UserQuery userQuery, UserQueryData location, HttpServletRequest req) {

    }
}
