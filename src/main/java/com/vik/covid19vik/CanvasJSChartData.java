package com.vik.covid19vik;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CanvasJSChartData {
    static HashMap<Object, Object> dataPoint;

    // method for taking in linked list of dates and linked list of cases and converting to object of
    // x/y set of points for canvas script to decipher as object of data points
    static Map<Object, Object>[] convertToXYPoints(LinkedList<String> dates, LinkedList<Integer> cases) throws ParseException {
        List<Map<Object, Object>> dataPoints = new LinkedList<>();
        int numberOfPoints = dates.size();
        for (int i = 0; i < numberOfPoints; i++) {
            dataPoint = new HashMap<>();
            String oldDate = dates.get(i);
            String newDate = simpleDateFormat(oldDate);
            dataPoint.put("label", newDate);
            dataPoint.put("y", cases.get(i));
            dataPoints.add(dataPoint);
        }
        Map<Object, Object>[] dataPointsArray = new Map[dataPoints.size()];
        for (int i = 0; i < dataPointsArray.length; i++) {
            dataPointsArray[i] = dataPoints.get(i);
        }
        return dataPointsArray;
    }

    static String simpleDateFormat(String oldDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
        return oldDate.replace("/", "-");
    }
}
