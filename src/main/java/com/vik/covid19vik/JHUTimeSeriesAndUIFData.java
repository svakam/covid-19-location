package com.vik.covid19vik;

public class JHUTimeSeriesAndUIFData {
    // consider adding to database:
    private CountriesGlobal confDataGlobal;
    private CountriesGlobal deathsDataGlobal;
    private CountriesGlobal recovDataGlobal;
    private USTimeSeries confDataUS;
    private USTimeSeries deathsDataUS;
    private UIFLookup[] uifData;

    CountriesGlobal getConfDataGlobal() {
        return confDataGlobal;
    }
    void setConfDataGlobal(CountriesGlobal confDataGlobal) {
        this.confDataGlobal = confDataGlobal;
    }
    CountriesGlobal getDeathsDataGlobal() {
        return deathsDataGlobal;
    }
    void setDeathsDataGlobal(CountriesGlobal deathsDataGlobal) {
        this.deathsDataGlobal = deathsDataGlobal;
    }
    CountriesGlobal getRecovDataGlobal() {
        return recovDataGlobal;
    }
    void setRecovDataGlobal(CountriesGlobal recovDataGlobal) {
        this.recovDataGlobal = recovDataGlobal;
    }
    USTimeSeries getConfDataUS() {
        return confDataUS;
    }
    void setConfDataUS(USTimeSeries confDataUS) {
        this.confDataUS = confDataUS;
    }
    USTimeSeries getDeathsDataUS() {
        return deathsDataUS;
    }
    void setDeathsDataUS(USTimeSeries deathsDataUS) {
        this.deathsDataUS = deathsDataUS;
    }
    UIFLookup[] getUifData() {
        return uifData;
    }
    void setUifData(UIFLookup[] uifData) {
        this.uifData = uifData;
    }
}
