package com.vik.covid19vik;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestJHUCSSEPull {
    void get() {
        URL url = null;
        try {
            url = new URL("https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/UID_ISO_FIPS_LookUp_Table.csv");
        } catch (
                MalformedURLException e) {
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
                countrySummary = gson.fromJson(in, SummaryCasesByCountry.class);
            }

            in.close();
            con.disconnect();

        } catch (
                IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
