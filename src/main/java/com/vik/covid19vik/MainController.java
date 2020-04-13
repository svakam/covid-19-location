package com.vik.covid19vik;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

@Controller
public class MainController {

    // consider adding to database
    String globalConfData = JHUPullMethods.getTimeSeriesGlobalConf();
//    String globalDeathsData = TimeSeriesPullMethods.getTimeSeriesGlobalDeaths();
//    String globalRecovData = TimeSeriesPullMethods.getTimeSeriesGlobalRecov();
    CountryUIFLookup[] countries = UIFLookupParse.fromJSON();

    // index
    @GetMapping("/")
    public String getIndex(Model model) {

//        CountryGlobalDataParse.parseData("confirmed", globalConfData);
        LinkedList<String> countryDropdown = new LinkedList<>();
        for (CountryUIFLookup country : countries) {
            if (!countryDropdown.contains(country.getCountryOrRegion())) {
                countryDropdown.add(country.getCountryOrRegion());
            }
        }

        // create list of countries

        model.addAttribute("countryNames", countryDropdown);
        return "index";
    }

    // post request with user's country name search
    @PostMapping("/results/country")
    public RedirectView submitCountrySearch(String searchedCountry) throws UnsupportedEncodingException {

        System.out.println("Dropdown selected = " + searchedCountry);

        RedirectView rv;

        return rv;
    }

//    @PostMapping("/results/country/province")
//    public RedirectView submitProvinceSearch(String searchedProvince, String countryForProvince) {
//        return rv;
//    }


    @GetMapping("/results/country")
    public String resultsForCountry(Model model) {


        return "countryResults";
    }

    @GetMapping("/results/country/province")
    public String resultsForProvince(Model model) {

        return "provinceResults";
    }

    // error for slug
    @GetMapping("/error/404/{searchedCountry}")
    public String error404(Model model) {

        return "error404";
    }

    // fallback
    @GetMapping("*")
    public String fallback(Model model) {
        return "error404";
    }
}
