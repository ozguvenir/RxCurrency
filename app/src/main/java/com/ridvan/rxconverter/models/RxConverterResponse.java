package com.ridvan.rxconverter.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class RxConverterResponse {
    @SerializedName("base")
    private String base;
    @SerializedName("date")
    private String date;
    @SerializedName("rates")
    private HashMap<String, Double> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }
}
