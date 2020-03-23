package com.vik.covid19vik;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CountrySummaryCases {
    private Country[] countries;

    static class Country {
        private String Country;
        private String Slug;
        private int NewConfirmed;
        private int TotalConfirmed;
        private int NewDeaths;
        private int TotalDeaths;
        private int NewRecovered;
        private int TotalRecovered;

        public Country() {
            // no args constructor for Country
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

    public CountrySummaryCases() {
        // no args constructor for CountrySummaryCases
    }

    public Country[] getCountry() {
        return countries;
    }
    public String getDate() {
        return Date;
    }

    static void getCountryCases(String endpoint) {
        URL url = null;
        try {
            url = new URL(endpoint);
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

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
