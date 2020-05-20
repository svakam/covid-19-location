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
    // ========================= time series global data ========================= //
    static CountriesGlobal getTimeSeriesConf(HttpServletRequest req) {
        try {
            URL url;
            String requestURL = req.getRequestURL().toString();
            String baseURL = extractSecondDomainURL(requestURL);
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
    static CountriesGlobal getTimeSeriesDeaths(HttpServletRequest req) {
        try {
            URL url;
            String requestURL = req.getRequestURL().toString();
            String baseURL = extractSecondDomainURL(requestURL);
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
    static CountriesGlobal getTimeSeriesRecov(HttpServletRequest req) {
        try {
            URL url;
            String requestURL = req.getRequestURL().toString();
            String baseURL = extractSecondDomainURL(requestURL);
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
    private static CountriesGlobal timeSeriesHttpCall(URL url) {
        try {
            CountriesGlobal timeSeries;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;

            int status = con.getResponseCode();
            if (status > 299) {
                StringBuilder content = new StringBuilder();
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                System.out.println(content.toString());
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // convert json to desired output and return
                Gson gson = new Gson();
                timeSeries = gson.fromJson(in, CountriesGlobal.class);
                return timeSeries;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }



    // ========================= time series US data ========================= //
    static USTimeSeries getTimeSeriesUSConf(HttpServletRequest req) {
        try {
            URL url;
            String requestURL = req.getRequestURL().toString();
            String baseURL = extractSecondDomainURL(requestURL);
            String fullURL = baseURL + "API/series/US/confirmed";
            url = new URL(fullURL);
            return timeSeriesUSHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    static USTimeSeries getTimeSeriesUSDeaths(HttpServletRequest req) {
        try {
            URL url;
            String requestURL = req.getRequestURL().toString();
            String baseURL = extractSecondDomainURL(requestURL);
            String fullURL = baseURL + "API/series/US/deaths";
            url = new URL(fullURL);
            return timeSeriesUSHttpCall(url);
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private static USTimeSeries timeSeriesUSHttpCall(URL url) {
        try {
            USTimeSeries timeSeries;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;

            int status = con.getResponseCode();
            if (status > 299) {
                StringBuilder content = new StringBuilder();
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                System.out.println(content.toString());
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // convert json to desired output and return
                Gson gson = new Gson();
                timeSeries = gson.fromJson(in, USTimeSeries.class);
                return timeSeries;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }




    // ========================= UIF data ========================= //
    static UIFLookup[] getUIFLookup(HttpServletRequest req) {
        String requestURL = req.getRequestURL().toString();
        String baseURL = extractSecondDomainURL(requestURL);
        try {
            URL url;
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
    private static UIFLookup[] UIFHttpCall(URL url) {
        try {
            UIFLookup[] UIFData;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/html;charset=UTF-8");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;

            int status = con.getResponseCode();
            if (status > 299) {
                StringBuilder content = new StringBuilder();
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                System.out.println(content.toString());
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                // convert json to desired output and return
                Gson gson = new Gson();
                UIFData = gson.fromJson(in, UIFLookup[].class);
                return UIFData;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    // ========================= parse second domain from URL ========================= //
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
