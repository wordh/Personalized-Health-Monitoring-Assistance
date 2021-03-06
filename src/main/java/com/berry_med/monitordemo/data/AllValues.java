package com.berry_med.monitordemo.data;

/**
 * Created by Wordh Ul Hasan on 3/17/2018.
 */

public class AllValues {

    private String name;
    private String age;
    private int heartRate;
    private int restRate;
    private int highPressure;
    private int meanPressure;
    private int lowPressure;
    private int SpO2;
    private int pulseRate;
    private double temperature;
    private int ecgStatus;
    private int pulseStatus;


    public int getEcgStatus() {
        return ecgStatus;
    }

    public void setEcgStatus(int ecgStatus) {
        this.ecgStatus = ecgStatus;
    }

    public int getPulseStatus() {
        return pulseStatus;
    }

    public void setPulseStatus(int pulseStatus) {
        this.pulseStatus = pulseStatus;
    }
    public String getName() {
        return name;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String heartRate) {
        this.age = heartRate;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getRestRate() {
        return restRate;
    }

    public void setRestRate(int restRate) {
        this.restRate = restRate;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public int getMeanPressure() {
        return meanPressure;
    }

    public void setMeanPressure(int meanPressure) {
        this.meanPressure = meanPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }

    public int getSpO2() {
        return SpO2;
    }

    public void setSpO2(int spO2) {
        SpO2 = spO2;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
