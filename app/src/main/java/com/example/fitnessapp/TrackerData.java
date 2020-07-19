package com.example.fitnessapp;

import java.io.Serializable;

public class TrackerData implements Serializable {

    private int calories;
    private String foodType;
    private int quantity;
    private String measurement;

    private String date;

    private String time;
    public TrackerData(int calories, String foodType, int quantity, String measurement, String date, String time) {
        this.calories = calories;
        this.foodType = foodType;
        this.quantity = quantity;
        this.measurement = measurement;
        this.date = date;
        this.time = time;
    }

    public TrackerData() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getCalories() {
        return calories;
    }

    public String getFoodType() {
        return foodType;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}