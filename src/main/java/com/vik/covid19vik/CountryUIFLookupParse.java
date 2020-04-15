package com.vik.covid19vik;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.LinkedList;

class CountryUIFLookupParse {

    protected static String parseDatatoJSON() {

        // pull data
        String data = JHUPullMethods.getUIFLookup();
//        System.out.println(data);

        LinkedList<CountryUIFLookup> countries = new LinkedList<>();
        int cursor = 0;
        int lengthOfCSV = data.length();
        System.out.println(lengthOfCSV);

        // loop over data until a given string equals "Population"
        while (true) {
            StringBuilder labelB = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                labelB.append(data.charAt(cursor));
                cursor++;
                if (labelB.toString().equals("Population\n")) {
                    break;
                }
            }
            if (labelB.toString().equals("Population\n")) {
                break;
            }
            cursor++;
        }

        // instantiate data of each country and store in array
        do {
            CountryUIFLookup country = new CountryUIFLookup();

            // set UID (always available)
            StringBuilder uid = new StringBuilder();
            while (data.charAt(cursor) != ',') {
                uid.append(data.charAt(cursor));
                cursor++;
            }
            country.setUid(Integer.parseInt(uid.toString()));
//            System.out.println("UID = " + uid.toString());
            cursor++;

            // set iso2
            if (data.charAt(cursor) == ',') {
                country.setIso2("");
            } else {
                StringBuilder iso2 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    iso2.append(data.charAt(cursor));
                    cursor++;
                }
                country.setIso2(iso2.toString());
//                System.out.println("ISO2 = " + iso2.toString());
            }
            cursor++;

            // set iso3
            if (data.charAt(cursor) == ',') {
                country.setIso3("");
            } else {
                StringBuilder iso3 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    iso3.append(data.charAt(cursor));
                    cursor++;
                }
                country.setIso3(iso3.toString());
//                System.out.println("ISO3 = " + iso3.toString());
            }
            cursor++;

            // set code3
            if (data.charAt(cursor) == ',') {
                country.setCode3(-1);
//                System.out.println("code3 = " + country.getCode3());
            } else {
                StringBuilder code3 = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    code3.append(data.charAt(cursor));
                    cursor++;
                }
                country.setCode3(Integer.parseInt(code3.toString()));
//                System.out.println("code3 = " + code3.toString());
            }
            cursor++;

            // set FIPS
            if (data.charAt(cursor) == ',') {
                country.setFips("");
//                System.out.println("fips = empty");
            } else {
                StringBuilder fips = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    fips.append(data.charAt(cursor));
                    cursor++;
                }
                country.setFips(fips.toString());
//                System.out.println("fips = " + fips.toString());
            }
            cursor++;

            // set County
            if (data.charAt(cursor) == ',') {
                country.setCounty("");
//                System.out.println("county = empty");
            } else {
                StringBuilder county = new StringBuilder();
                while (data.charAt(cursor) != ',') {
                    county.append(data.charAt(cursor));
                    cursor++;
                }
                country.setIso3(county.toString());
//                System.out.println(county.toString());
            }
            cursor++;

            // set province/state
            if (data.charAt(cursor) == ',') {
                country.setProvinceOrState("");
//                System.out.println("province/state = empty");
            } else {
                StringBuilder provinceOrState = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        provinceOrState.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        provinceOrState.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                country.setProvinceOrState(provinceOrState.toString());
//                System.out.println("province/state = " + provinceOrState.toString());
            }
            cursor++;

            // set country/region
            if (data.charAt(cursor) == ',') {
                country.setCountryOrRegion("");
//                System.out.println("country/region = empty");
            } else {
                StringBuilder countryRegion = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        countryRegion.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        countryRegion.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                country.setCountryOrRegion(countryRegion.toString());
//                System.out.println("country/region = " + countryRegion.toString());
            }
            cursor++;

            // set lat/long
            StringBuilder lat = new StringBuilder();
            if (data.charAt(cursor) == ',') {
                country.setLat(5555);
//                System.out.println("lat = " + country.getLat());
            } else {
                while (data.charAt(cursor) != ',') {
                    lat.append(data.charAt(cursor));
                    cursor++;
                }
                country.setLat(Float.parseFloat(lat.toString()));
//                System.out.println("lat = " + Float.parseFloat(lat.toString()));
            }
            cursor++;
            StringBuilder lon = new StringBuilder();
            if (data.charAt(cursor) == ',') {
                country.setLon(5555);
//                System.out.println("lat = " + country.getLon());
            } else {
                while (data.charAt(cursor) != ',') {
                    lon.append(data.charAt(cursor));
                    cursor++;
                }
                country.setLon(Float.parseFloat(lon.toString()));
//                System.out.println("lon = " + Float.parseFloat(lon.toString()));
            }
            cursor++;

            // set combined key
            if (data.charAt(cursor) == ',') {
                country.setCombinedKey("");
//                System.out.println("combined key = empty");
            } else {
                StringBuilder combinedKey = new StringBuilder();
                if (data.charAt(cursor) == '\"') {
                    cursor++;
                    while (data.charAt(cursor) != '\"') {
                        combinedKey.append(data.charAt(cursor));
                        cursor++;
                    }
                    cursor++;
                } else {
                    while (data.charAt(cursor) != ',') {
                        combinedKey.append(data.charAt(cursor));
                        cursor++;
                    }
                }
                country.setCombinedKey(combinedKey.toString());
//                System.out.println("combined key = " + combinedKey.toString());
            }
            cursor++;

            // set population
            if (data.charAt(cursor) == '\n') {
                country.setPopulation(0);
//                System.out.println("population = " + country.getPopulation());
            } else {
                StringBuilder population = new StringBuilder();
                while (data.charAt(cursor) != '\n') {
                    population.append(data.charAt(cursor));
                    cursor++;
                    if (cursor >= lengthOfCSV) {
                        break;
                    }
                }
                country.setPopulation(Integer.parseInt(population.toString()));
//                System.out.println("population = " + Integer.parseInt(population.toString()));
            }
            cursor++;
            countries.add(country);
        } while (cursor < lengthOfCSV);

        // convert to json
        Gson gson = new Gson();
        return gson.toJson(countries);
    }

    protected static CountryUIFLookup[] fromJSON() {
        String json = parseDatatoJSON();
        Gson gson = new Gson();
        return gson.fromJson(json, CountryUIFLookup[].class);
    }
}
