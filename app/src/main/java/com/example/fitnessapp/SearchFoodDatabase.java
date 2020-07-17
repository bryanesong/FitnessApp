package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class SearchFoodDatabase extends AppCompatActivity {

    private ArrayList<USDAFoodParser.FoodEntry> SFDfoodEntries= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USDAFoodParser parser = new USDAFoodParser();
        //SFDfoodEntries = parser.searchFood("Cheddar Cheese");


        Log.e("SearchFoodDatabase", "" + parser.getFoodList().size());
        setContentView(R.layout.activity_search_food_database);
        initializeRecyclerView();

    }

    private void initializeRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.SFDrecyclerView);
        SearchFoodDatabaseViewAdapter adapter = new SearchFoodDatabaseViewAdapter(SFDfoodEntries, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}