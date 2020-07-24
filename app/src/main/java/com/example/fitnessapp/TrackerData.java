package com.example.fitnessapp;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackerData implements Serializable, Comparable<TrackerData>{

    private int calories;
    private String foodType;
    private int quantity;
    private String measurement;
    private String date;
    private String time;

    private boolean isDateData;
    private String dateText;


    public TrackerData(boolean isDateData, String dateText) {
        this.isDateData = isDateData;
        this.dateText = dateText;
    }

    public TrackerData(int calories, String foodType, int quantity, String measurement, String date, String time, boolean isDateData) {
        this.calories = calories;
        this.foodType = foodType;
        this.quantity = quantity;
        this.measurement = measurement;
        this.date = date;
        this.time = time;
        this.isDateData = isDateData;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setDateData(boolean dateData) {
        this.isDateData = isDateData;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }



    public boolean isDateData() {
        return isDateData;
    }
    public String getDateText() {
        return dateText;
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

    @Override
    public int compareTo(TrackerData trackerData) {
        DateFormat simp = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date mDate = new Date();
        Date oDate =  new Date();

        try {
             mDate = simp.parse(getDate() + " " + getTime());
             oDate = simp.parse(trackerData.getDate() + " " + trackerData.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return oDate.compareTo(mDate);
    }
}