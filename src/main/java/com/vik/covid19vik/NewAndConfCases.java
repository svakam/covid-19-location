package com.vik.covid19vik;

import java.util.LinkedList;

class NewAndConfCases {
    private LinkedList<Integer> newProvCases;
    private LinkedList<Integer> totalProvCases;

    // getters
    LinkedList<Integer> getNewProvCases() {
        return newProvCases;
    }
    LinkedList<Integer> getTotalProvCases() {
        return totalProvCases;
    }
    // setters
    void setNewProvCases(LinkedList<Integer> newProvCases) {
        this.newProvCases = newProvCases;
    }
    void setTotalProvCases(LinkedList<Integer> totalProvCases) {
        this.totalProvCases = totalProvCases;
    }
}
