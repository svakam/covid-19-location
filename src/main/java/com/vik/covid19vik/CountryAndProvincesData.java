package com.vik.covid19vik;

import com.google.gson.Gson;

import javax.validation.constraints.Null;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class CountryAndProvincesData implements Serializable {
    private String Country;
    private String Province;
    private float Lat;
    private float Lon;
    private String Date;
    private int Cases;
    private String Status;

    CountryAndProvincesData() {
        // empty arg constructor
    }

    public String getCountry() {
        return Country;
    }
    public String getProvince() {
        return Province;
    }
    public float getLat() {
        return Lat;
    }
    public float getLon() {
        return Lon;
    }
    public String getDate() {
        return Date;
    }
    public int getCases() {
        return Cases;
    }
    public String getStatus() {
        return Status;
    }

    @Override
    public String toString() {
        return "CountryAndProvincesData{" +
                "Country='" + Country + '\'' +
                ", Province='" + Province + '\'' +
                ", Lat='" + Lat + '\'' +
                ", Lon='" + Lon + '\'' +
                ", Date='" + Date + '\'' +
                ", Cases=" + Cases +
                ", Status='" + Status + '\'' +
                '}';
    }

    private static CountryAndProvincesData[] connectAndDeserialize(URL url) {
        CountryAndProvincesData[] timesSeriesData = null;
        try {
            assert url != null;
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
                Gson gson = new Gson();
                timesSeriesData = gson.fromJson(in, CountryAndProvincesData[].class);
            }

            in.close();
            con.disconnect();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return timesSeriesData;
    }

    // GET request returns confirmed cases for time series of country since day 1 of its outbreak (and provinces if available)
    protected static LinkedList<CountryAndProvincesData[]> getTimeSeriesData(String countrySlug) {
        URL url = null;

        LinkedList<CountryAndProvincesData[]> data = new LinkedList<>();

        // confirmed cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "/status/confirmed");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            assert url != null;
            System.out.println(url);
            CountryAndProvincesData[] timeSeriesConfirmed = connectAndDeserialize(url);
            data.add(timeSeriesConfirmed);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // deaths cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "/status/deaths");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            CountryAndProvincesData[] timeSeriesDeaths = connectAndDeserialize(url);
            data.add(timeSeriesDeaths);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // recovered cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "/status/recovered");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            CountryAndProvincesData[] timeSeriesRecovered = connectAndDeserialize(url);
            data.add(timeSeriesRecovered);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return data;
    }
}
