package com.example.fitnessapp;

import java.util.ArrayList;

public class TrackerDataContainer {

    String yert = "HI";
    private ArrayList<TrackerData> entries = new ArrayList<TrackerData>();

    public ArrayList<TrackerData> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TrackerData> entries) {
        this.entries = entries;
    }

    public void setYert(String yert) {
        this.yert = yert;
    }

    public String getYert() {
        return yert;
    }

    public TrackerDataContainer() {
        //entries.add(new TrackerData(4,"cheese",6,"oz"));
    }


}
