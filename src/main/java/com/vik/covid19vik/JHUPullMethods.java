package com.vik.covid19vik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class JHUPullMethods {
    // --------------------- get time series data -------------------- //
    static String getTimeSeriesGlobalConf() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }
    static String getTimeSeriesGlobalDeaths() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv");
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }
    static String getTimeSeriesGlobalRecov() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv");
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }
    static String getTimeSeriesUSConf() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }
    static String getTimeSeriesUSDeaths() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_US.csv");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }

    // -------- get UID/ISO/FIPS and country/province/county name info -------- //
    static String getUIFLookup() {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/UID_ISO_FIPS_LookUp_Table.csv");
        } catch (
                MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        assert url != null;
        return httpCall(url);
    }

    // http url connection
    static String httpCall(URL url) {
        String pull = null;
        try {
            assert url != null;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;
            StringBuilder content;

            int status = con.getResponseCode();
            if (status > 299) {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                System.out.println(content.toString());
                throw new NullPointerException("Error: not able to GET from " + url);
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                pull = content.toString();
            }

            in.close();
            con.disconnect();
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return pull;
    }
}
