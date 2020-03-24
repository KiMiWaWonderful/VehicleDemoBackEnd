package com.example.demo.pojo;

public class CarData {

    private double weatherData;
    private double moistData;
    private double smokeData;
    private double alcoholData;

    public CarData(double weatherData, double moistData, double smokeData, double alcoholData) {
        this.weatherData = weatherData;
        this.moistData = moistData;
        this.smokeData = smokeData;
        this.alcoholData = alcoholData;
    }

    public CarData() {
    }

    public double getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(double weatherData) {
        this.weatherData = weatherData;
    }

    public double getMoistData() {
        return moistData;
    }

    public void setMoistData(double moistData) {
        this.moistData = moistData;
    }

    public double getSmokeData() {
        return smokeData;
    }

    public void setSmokeData(double smokeData) {
        this.smokeData = smokeData;
    }

    public double getAlcoholData() {
        return alcoholData;
    }

    public void setAlcoholData(double alcoholData) {
        this.alcoholData = alcoholData;
    }
}
