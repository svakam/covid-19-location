package com.vik.covid19vik;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class DateTimeFormatterTest {
    @Test
    void simpleDateFormat() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
        String oldDate = "1/20/20";
        oldDate = oldDate.replace("/", "-");
        Date date = dateFormat.parse(oldDate);
        assertTrue(date.toString().contains("Jan"));
        assertTrue(date.toString().contains("20"));
        assertTrue(date.toString().contains("2020"));
        System.out.println(date);
    }

    @Test
    LinkedList<String> parseDate() {
        String oldDate = "1/20/20";
        LinkedList<String> mdy = new LinkedList<>();
        int lengthDate = oldDate.length();
        StringBuilder b = new StringBuilder();
        String month;
        String day;
        String year;
        char[] chars = oldDate.toCharArray();
        int i = 0;
        while (chars[i] != '/') {
            char aChar = chars[i];
            b.append(aChar);
            i++;
        }
        i++;
        month = b.toString();
        assertEquals("1", month);
        mdy.add(month);
        StringBuilder c = new StringBuilder();
        while (chars[i] != '/') {
            char aChar = chars[i];
            c.append(aChar);
            i++;
        }
        i++;
        day = c.toString();
        assertEquals("20", day);
        mdy.add(day);
        StringBuilder d = new StringBuilder();
        while (i != lengthDate) {
            char aChar = chars[i];
            d.append(aChar);
            i++;
        }
        year = d.toString();
        assertEquals("20", year);
        mdy.add(year);
        return mdy;
    }

    @Test
    void makeJavaScriptDate() {
        LinkedList<String> mdy = parseDate();
        String month = mdy.get(0);
        String day = mdy.get(1);
        String year = mdy.get(2);
        // if day is 1 digit long, add a 0 at the beginning
        if (day.length() == 1) {
            day = "0" + day;
        }
        assertEquals("20", day);
        // if month is 1 digit long, add a 0 at the beginning
        if (month.length() == 1) {
            month = "0" + month;
        }
        assertEquals("01", month);
        // if year is 2 digits long, add a 20 at the beginning
        if (year.length() == 2) {
            year = "20" + year;
        }
        assertEquals("2020", year);

        String jsDate = year + "-" + month + "-" + day + "T00:00:00.000Z";
        assertEquals("2020-01-20T00:00:00.000Z", jsDate);
    }

    @Test
    void calendar() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 20);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 2020);
        Date dateRepresentation = cal.getTime();
        System.out.println(dateRepresentation);
    }


}
