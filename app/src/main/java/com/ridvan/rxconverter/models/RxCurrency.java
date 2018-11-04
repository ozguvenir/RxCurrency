package com.ridvan.rxconverter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class RxCurrency {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private Double value;

    public RxCurrency(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
