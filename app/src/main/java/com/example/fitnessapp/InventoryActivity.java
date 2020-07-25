package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class InventoryActivity extends AppCompatActivity {
    private ImageView itemOnPlayer_Hat,itemOnPlayer_Shirt,itemOnPlayer_Pants,itemOnPlayer_RightItem,itemOnPlayer_LeftItem,itemOnPlayer_Shoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initializeImageViews();//initalize all image views



    }

    public void initializeImageViews(){
        itemOnPlayer_Hat = findViewById(R.id.itemOnPlayer_Hat);
        itemOnPlayer_Shirt = findViewById(R.id.itemOnPlayer_Shirt);
        itemOnPlayer_RightItem = findViewById(R.id.itemOnPlayer_RightItem);
        itemOnPlayer_LeftItem = findViewById(R.id.itemOnPlayer_LeftItem);
        itemOnPlayer_Shoes = findViewById(R.id.itemOnPlayer_Shoes);
    }

    //this method will reach out and grab all iventory that the player has currently equipped
    public void retrieveAndUpdateInventory{

    }
}