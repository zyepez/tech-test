package com.citytemperature.temperaturews.models;

public class Temperature {

    private String nameCity;
    private Integer tempC;
    private Integer tempF;

    public Temperature() {
    }

    public Temperature(String nameCity, Integer tempC, Integer tempF) {
        this.nameCity = nameCity;
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public Integer getTempC() {
        return tempC;
    }

    public void setTempC(float tempC) {
        this.tempC = (int) tempC;
    }

    public Integer getTempF() {
        return tempF;
    }

    public void setTempF(float tempF) {
        this.tempF = (int) (tempF * 1.8000 + 32.00);
    }
}
