package com.example.fitnessapp;

import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InventoryInfoContainer {
    private ArrayList<ShopItem> items = new ArrayList<>();

    public InventoryInfoContainer() {

    }

    public InventoryInfoContainer(ArrayList<ShopItem> items) {
        this.items = items;
    }

    public void addShopItem(String name, int typeNum, int price, String description, int imageResource) {
        items.add(new ShopItem(name, typeNum, price, description, imageResource));
    }

    @NotNull
    public String toString(){
        String s = "";
        for(ShopItem thing : items){
            s+= thing.toString() +" ";
        }
        return s;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShopItem> items) {
        this.items = items;
    }

}
