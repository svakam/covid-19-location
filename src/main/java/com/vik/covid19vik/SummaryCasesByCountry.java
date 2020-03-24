package com.vik.covid19vik;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// https:/api.covid19api.com/summary - 1st level
public class SummaryCasesByCountry {
    private CountrySummary[] Countries;

    SummaryCasesByCountry() {
       // no args constructor
    }

    // https:/api.covid19api.com/summary - object type in array of countries
    static class CountrySummary {
        private String Country;
        private String Slug;
        private int NewConfirmed;
        private int TotalConfirmed;
        private int NewDeaths;
        private int TotalDeaths;
        private int NewRecovered;
        private int TotalRecovered;

        CountrySummary() {
            // no args constructor
        }

        public String getCountry() {
            return Country;
        }

        public String getSlug() {
            return Slug;
        }

        public int getNewConfirmed() {
            return NewConfirmed;
        }

        public int getTotalConfirmed() {
            return TotalConfirmed;
        }

        public int getNewDeaths() {
            return NewDeaths;
        }

        public int getTotalDeaths() {
            return TotalDeaths;
        }

        public int getNewRecovered() {
            return NewRecovered;
        }

        public int getTotalRecovered() {
            return TotalRecovered;
        }
    }


    private String Date;

    public CountrySummary[] getCountries() {
        return Countries;
    }

    public String getDate() {
        return Date;
    }

    // GET request returns list of countries w/ confirmed, deaths, recovered cases and time of data
    static SummaryCasesByCountry getCountriesCases() {
        SummaryCasesByCountry countrysummary = null;
        URL url = null;
        try {
            url = new URL("https://api.covid19api.com/summary");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
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
                countrysummary = gson.fromJson(in, SummaryCasesByCountry.class);
            }

            in.close();
            con.disconnect();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return countrysummary;
    }
}