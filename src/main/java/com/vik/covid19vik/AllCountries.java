package com.vik.covid19vik;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class AllCountries implements Serializable {
    private String Country;
    private String Slug;
    private String[] Provinces;

    AllCountries() {
        // no-args constructor
    }

    public String getCountry() {
        return Country;
    }
    public String getSlug() {
        return Slug;
    }
    public String[] getProvinces() {
        return Provinces;
    }

    @Override
    public String toString() {
        return "Country='" + Country + '\'' +
                ", Slug='" + Slug + '\'' +
                ", Provinces=" + Arrays.toString(Provinces);
    }

    // GET request returns array of countries
    static AllCountries[] getCountries() {
        URL url = null;
        AllCountries[] countries = null;
        try {
            url = new URL("https://api.covid19api.com/countries");
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        try {
            assert url != null;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            System.out.println(StatusMessageHeader.getInfo(con));

            BufferedReader in;
//            StringBuilder content = new StringBuilder();
//            String inputLine;

            int status = con.getResponseCode();
            if (status > 299) {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
            } else {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                Gson gson = new Gson();
                countries = gson.fromJson(in, AllCountries[].class);
            }
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine).append("\n");
//            }

            // timeout methods if needed
            // con.setConnectTimeout(5000);
            // con.setReadTimeout(5000);

            // add parameters to request if needed
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("param1", "val");

//        con.setDoOutput(true);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//        out.flush();
//        out.close();

            in.close();
            con.disconnect();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return countries;
    }


}
