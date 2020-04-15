package com.vik.covid19vik;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ApiMethods {
    // -------------- time series data -------------- //
    protected static CountryGlobal getTimeSeriesConf(HttpServletRequest req) {
        try {
            String baseURL = req.getRequestURL().toString();
            URL url = null;
            String fullURL = baseURL + "API/series/global/confirmed";
            url = new URL(fullURL);
            return timeSeriesHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    protected static CountryGlobal getTimeSeriesDeaths(HttpServletRequest req) {
        try {
            URL url = null;
            String baseURL = req.getRequestURL().toString();
            String fullURL = baseURL + "API/series/global/deaths";
            url = new URL(fullURL);
            return timeSeriesHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    protected static CountryGlobal getTimeSeriesRecov(HttpServletRequest req) {
        try {
            URL url = null;
            String baseURL = req.getRequestURL().toString();
            String fullURL = baseURL + "API/series/global/recovered";
            url = new URL(fullURL);
            return timeSeriesHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private static CountryGlobal timeSeriesHttpCall(URL url) {
        try {
            CountryGlobal timeSeries;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;

            int status = con.getResponseCode();
            if (status > 299) {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // convert json to desired output and return
                Gson gson = new Gson();
                timeSeries = gson.fromJson(in, CountryGlobal.class);
                return timeSeries;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // -------------- UIF data ------------- //
    protected static CountryUIFLookup[] getUIFLookup(HttpServletRequest req) {
        String requestURL = req.getRequestURL().toString();
        String baseURL = extractSecondDomainURL(requestURL);
        try {
            URL url = null;
            String fullURL = baseURL + "API/uifcountries";
            url = new URL(fullURL);
            return UIFHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private static CountryUIFLookup[] UIFHttpCall(URL url) {
        try {
            CountryUIFLookup[] UIFData;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/html;charset=UTF-8");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;

            int status = con.getResponseCode();
            if (status > 299) {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // convert json to desired output and return
                Gson gson = new Gson();
                UIFData = gson.fromJson(in, CountryUIFLookup[].class);
                return UIFData;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String extractSecondDomainURL(String url) {
        String regex = "(http:\\/\\/(www.)?\\w+(:|.)(5000|com)\\/)";
        Pattern compiled = Pattern.compile(regex);
        Matcher matcher = compiled.matcher(url);
        if (matcher.lookingAt()) {
            int beginning = matcher.start();
            int end = matcher.end();
            return url.substring(beginning, end);
        } else {
            return null;
        }
    }
}
