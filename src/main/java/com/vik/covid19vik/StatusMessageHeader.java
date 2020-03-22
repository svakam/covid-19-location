package com.vik.covid19vik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;

public class StatusMessageHeader {
    public static String getFullResponse(HttpURLConnection con) throws IOException {
        StringBuilder fullResponseBuilder = new StringBuilder();

        // read status and message
        fullResponseBuilder.append(con.getResponseCode())
                .append(" ")
                .append(con.getResponseMessage())
                .append("\n");

        // read headers
        con.getHeaderFields().entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .forEach(entry -> {
                    fullResponseBuilder.append(entry.getKey()).append(": ");
                    List headerValues = entry.getValue();
                    Iterator it = headerValues.iterator();
                    if (it.hasNext()) {
                        fullResponseBuilder.append(it.next());
                        while (it.hasNext()) {
                            fullResponseBuilder.append(", ").append(it.next());
                        }
                    }
                    fullResponseBuilder.append("\n");
                });

        // read response content

        return fullResponseBuilder.toString();
    }
}
