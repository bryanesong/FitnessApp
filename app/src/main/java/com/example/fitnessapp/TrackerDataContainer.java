package com.example.fitnessapp;

import java.util.ArrayList;

public class TrackerDataContainer {

    private ArrayList<TrackerData> entries = new ArrayList<TrackerData>();

    public ArrayList<TrackerData> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TrackerData> entries) {
        this.entries = entries;
    }

    public TrackerDataContainer() {

    }

    public TrackerDataContainer(ArrayList<TrackerData> data) {
        entries = data;
    }


}
