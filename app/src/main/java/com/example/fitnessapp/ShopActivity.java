package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    RecyclerView shopItemsRecyclerView;
    ShopAdapter shopAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopItemsRecyclerView = findViewById(R.id.shopItems);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemsRecyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<String> shopItemsFake = new ArrayList<>();
        shopItemsFake.add("water melon hat");
        shopItemsFake.add("forbidden fruits");
        shopItemsFake.add("rgb keyboard");
        shopItemsFake.add("'gaymer rise up' t-shirt");
        shopItemsFake.add("'I'm not a simp' tank top");
        shopItemsFake.add("Ultimate Abs Vibrator");
        shopItemsFake.add("Gamer Girl Bath Water");
        shopItemsFake.add("Simp License");

        shopAdapter = new ShopAdapter(ShopActivity.this, shopItemsFake);
        shopAdapter.notifyDataSetChanged();
        shopItemsRecyclerView.setAdapter(shopAdapter);


    }
}