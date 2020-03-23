package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import com.google.gson.Gson;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

//    @GetMapping("/greeting") // ensures HTTP GET requests to /greeting mapped to greeting() method
//    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
//        // @RequestParam binds value of query string parameter name into name parameter of greeting(); if name absent, "World" used instead
//        model.addAttribute("name", name);
//        return "greeting";
//    }

    // index
    @GetMapping("/")
    public String getIndex(Model model) throws IOException {
        // call to countries endpoint: contains country, slug, and array of provinces
        Countries[] countries = apiCallWithURLAndJSONConv("https://api.covid19api.com/countries");

        // add deserialized JSON as attribute to index
        model.addAttribute("countries", countries);

        // ideally some code that caches result of JSONResult or stores in database, and checks to see if anything's changed after a day or since last update
        // would avoid redundant api call

        return "index";
    }

    // post request with user's search
    @PostMapping("/search")
    public RedirectView submitSearch(String country) {
        System.out.println("country = " + country);
        // submit form input with country info
        // create country instance
        // redirect to results
        return new RedirectView("/");
    }

    // get request to covid19api: https://api.covid19api.com/
    @GetMapping("/results")
    public String covid19api(Model model) {
        apiCallWithURLAndJSONConv("https://api.covid19api.com/countries");

        // deserialize JSON output
        Countries searchedByCountry = new Countries();

        // return output
//        model.addAttribute("result", JSONResult);
        return "results";
    }

    // api call method takes in an endpoint and returns a string(builder)
    private Countries[] apiCallWithURLAndJSONConv(String endpoint) {
        URL url = null;
        Countries[] countries = null;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
//            return e.getMessage();
        }
        try {
            assert url != null;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Gson gson = new Gson();

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
                countries = gson.fromJson(in, Countries[].class);
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
