package com.example.fitnessapp;

import java.util.ArrayList;

public class InventoryInfoContainer {
    private int coins;
    private ArrayList<String> items = new ArrayList<>();


    public InventoryInfoContainer(int coins, ArrayList<String> items) {
        this.coins = coins;
        this.items = items;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }



}
