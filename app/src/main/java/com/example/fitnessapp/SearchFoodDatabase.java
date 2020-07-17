package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFoodDatabase extends AppCompatActivity {
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";
    USDAFoodParser parser;

    private ArrayList<USDAFoodParser.FoodEntry> SFDfoodEntries= new ArrayList<>();
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //SFDfoodEntries = parser.random("Cheddar Cheese");
        placeholder("Cheddar Cheese");
    }



    private void initializeRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.SFDrecyclerView);
        SearchFoodDatabaseViewAdapter adapter = new SearchFoodDatabaseViewAdapter(SFDfoodEntries, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void placeholder(final String foodName){
        AsyncTask asyncTask = new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Log.d("test","https://api.nal.usda.gov/fdc/v1/foods/list?api_key="+apiKey+"&query="+foodName.toString().replace(" ","%20"));
                Request request = new Request.Builder()
                        .url("https://api.nal.usda.gov/fdc/v1/foods/list?api_key="+apiKey+"&query="+foodName.toString().replace(" ","%20"))
                        .build();

                Response response = null;

                try{
                    response = client.newCall(request).execute();
                    return response.body().string();
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o){
                //JSONText.setText(o.toString());
                //testing parsing JSON file.
                try {

                    JSONArray obj = new JSONArray(o.toString());
                    Log.d("test",obj.toString());
                    parser = new USDAFoodParser(obj);
                    foodHasBeenParsed(parser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }
    public void foodHasBeenParsed(USDAFoodParser parserLocal){

        Log.e("SearchFoodDatabase", "" + parserLocal.getFoodList().size());
        setContentView(R.layout.activity_search_food_database);
        initializeRecyclerView();
    }
}