package com.vik.covid19vik;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CountryAndProvincesData {
    private String Country;
    private String Province;
    private String Lat;
    private String Lon;
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
    public String getLat() {
        return Lat;
    }
    public String getLon() {
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
        CountryAndProvincesData[] timeSeriesCountryAndProvincesConfirmed = null;
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
                timeSeriesCountryAndProvincesConfirmed = gson.fromJson(in, CountryAndProvincesData[].class);
            }

            in.close();
            con.disconnect();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return timeSeriesCountryAndProvincesConfirmed;
    }

    // GET request returns confirmed cases for time series of country since day 1 of its outbreak (and provinces if available)
    public static CountryAndProvincesData[][] getCountries(String countrySlug) {
        URL url = null;

        CountryAndProvincesData[][] data = new CountryAndProvincesData[3][];

        // confirmed cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "status/confirmed");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            assert url != null;
            CountryAndProvincesData[] confirmed = connectAndDeserialize(url);
            data[0] = confirmed;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // deaths cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "status/deaths");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            CountryAndProvincesData[] deaths = connectAndDeserialize(url);
            data[1] = deaths;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // recovered cases
        try {
            url = new URL("https://api.covid19api.com/dayone/country/" + countrySlug + "status/recovered");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            CountryAndProvincesData[] recovered = connectAndDeserialize(url);
            data[2] = recovered;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return data;
    }
}
