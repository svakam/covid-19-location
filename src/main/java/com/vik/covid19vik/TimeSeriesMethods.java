package com.vik.covid19vik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TimeSeriesMethods {
    // get JHU info and return as String
    protected String getTimeSeriesGlobalConf() {
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

    protected String getTimeSeriesGlobalDeaths() {
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

    protected String getTimeSeriesGlobalRecov() {
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

    private String httpCall(URL url) {
        String pull = null;
        try {
            assert url != null;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;
            StringBuilder content = null;

            int status = con.getResponseCode();
            if (status > 299) {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }

            }

            in.close();
            con.disconnect();

            if (content == null) {
                throw new NullPointerException("Error: not able to GET from " + url);
            } else {
                pull = content.toString();
            }
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return pull;
    }
}
