package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    private static final String template = "Hello, %s!";

    @GetMapping("/greeting") // ensures HTTP GET requests to /greeting mapped to greeting() method
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        // @RequestParam binds value of query string parameter name into name parameter of greeting(); if name absent, "World" used instead
        model.addAttribute("name", name);
        return "greeting";
    }

    // from search box, render results for given state/county/zip code
    @GetMapping("/results")
    public String results(@RequestParam(value = "city", required = true, defaultValue = "US") String city, Model model) {
        model.addAttribute("city", city);
        return "results";
    }

    // get request to covid19api: https://api.covid19api.com/
    @GetMapping("/covid19api")
    public String covid19api() throws IOException {
        URL url = new URL("https://api.covid19api.com/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        int status = con.getResponseCode();
        System.out.println("status = " + status);

        BufferedReader in;
        StringBuilder content = new StringBuilder();

        System.out.println(StatusMessageHeader.getInfo(con));

        if (status > 299) {
            in = new BufferedReader(
                    new InputStreamReader(con.getErrorStream()));
        } else {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }

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

        // return JSON representation at this route
        in.close();
        con.disconnect();
        return content.toString();
    }
}
