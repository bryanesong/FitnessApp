package com.example.fitnessapp;

public class TrackerData {

    private int calories;
    private String foodType;
    private int quantity;
    private String measurement;

    public TrackerData(int calories, String foodType, int quantity, String measurement) {
        this.calories = calories;
        this.foodType = foodType;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public TrackerData() {

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
}