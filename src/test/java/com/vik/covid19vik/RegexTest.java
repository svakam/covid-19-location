package com.vik.covid19vik;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
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

    @Test
    void testPhoneNum() {
        String completeNum = "+12345678901";
        String regex = "\\+1\\d{10}";
        Pattern compiled = Pattern.compile(regex);
        assertEquals("\\+1\\d{10}", compiled.toString());
        Matcher matcher = compiled.matcher(completeNum);
        assertTrue(matcher.matches());

        String noCountryCode = "1234567890";
        String regex2 = "\\d{10}";
        Pattern compiled2 = Pattern.compile(regex2);
        assertEquals("\\d{10}", compiled2.toString());
        Matcher matcher2 = compiled2.matcher(noCountryCode);
        assertTrue(matcher2.matches());
        Matcher matcher3 = compiled2.matcher(completeNum);
        assertFalse(matcher3.matches());
    }
}
