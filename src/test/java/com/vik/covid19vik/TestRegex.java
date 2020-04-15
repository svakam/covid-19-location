package com.vik.covid19vik;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    @Test
    void extractSecondDomainURL() {
        String url = "http://localhost:5000/results/country";
        String regex = "(http:\\/\\/(www.)?\\w+(:|.)(5000|com)\\/)";
        Pattern compiled = Pattern.compile(regex);
        assertEquals("(http:\\/\\/(www.)?\\w+(:|.)(5000|com)\\/)", compiled.toString());
        Matcher matcher = compiled.matcher(url);
        assertTrue(matcher.lookingAt());
        assertEquals(0, matcher.start());
        assertEquals(22, matcher.end());
        String expected = "http://localhost:5000/";
        String baseURL = url.substring(0, 22);
        assertEquals(expected, baseURL);
    }
}
