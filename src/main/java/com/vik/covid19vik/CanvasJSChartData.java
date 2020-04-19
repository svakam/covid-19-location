package com.vik.covid19vik;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CanvasJSChartData {
    static HashMap<Object, Object> dataPoint;

    // method for taking in linked list of dates and linked list of cases and converting to object of
    // x/y set of points for canvas script to decipher as object of data points
    static Map<Object, Object>[] convertToXYPoints(LinkedList<String> dates, LinkedList<Integer> cases) {
        List<Map<Object, Object>> dataPoints = new LinkedList<>();
        int numberOfPoints = dates.size();
        for (int i = 0; i < numberOfPoints; i++) {
            dataPoint = new HashMap<>();
            dataPoint.put("x", dates.get(i));
            dataPoint.put("y", cases.get(i));
            dataPoints.add(dataPoint);
        }
        Map<Object, Object>[] dataPointsArray = new Map[dataPoints.size()];
        for (int i = 0; i < dataPointsArray.length; i++) {
            dataPointsArray[i] = dataPoints.get(i);
        }
        return dataPointsArray;
    }
}
