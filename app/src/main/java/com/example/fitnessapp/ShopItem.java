package com.example.fitnessapp;

import org.jetbrains.annotations.NotNull;

public class ShopItem{

    private String name = "";
    private String description = "";
    private ItemType type = null;
    private int price = 0;
    private int imageResource = 0;


    public enum ItemType{
        HAT,
        SHIRT,
        ARMS,
        PANTS,
        SHOES,
        NONE
    }

    public ShopItem(){
        name = "placeholder";
        type = ItemType.NONE;
    }

    public ShopItem(String name, int typeNum, int price, String description, int imageResource) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
        switch(typeNum){
            case 1:
                type = ItemType.HAT;
                break;
            case 2:
                type = ItemType.SHIRT;
                break;
            case 3:
                type = ItemType.ARMS;
                break;
            case 4:
                type = ItemType.PANTS;
                break;
            case 5:
                type = ItemType.SHOES;
                break;
            default:
                type = ItemType.NONE;
                break;
        }
    }

    @NotNull
    public String toString(){
        return "Name: "+name+" Type: "+type+" Price: "+price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}

