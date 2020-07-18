package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFoodDatabase extends AppCompatActivity {
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";

    private ArrayList<USDAFoodParser.FoodEntry> SFDfoodEntries= new ArrayList<>();
    SearchFoodDatabaseViewAdapter adapter = new SearchFoodDatabaseViewAdapter(SFDfoodEntries, this);

    private Button inputButton;
    TextView inputFood, dialogServingText, dialogOtherInfoText;
    EditText dialogServingInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_database);

        createInputButton();

        createTextView();

        createInputButtonListener();

        initializeRecyclerView();

        openDialog();


    }

    private void createInputButton() {
        inputButton = (Button)findViewById(R.id.SFDsearchButton);
    }



    private void createTextView() {
        inputFood = (TextView)findViewById(R.id.SFDFoodInput);

    }

    private void createInputButtonListener() {
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchFoodDatabase","Button clicked");
                if(!inputFood.getText().toString().equals("")) {
                    searchFood(inputFood.getText().toString());
                    inputFood.setText("");
                } else {
                    Toast.makeText(SearchFoodDatabase.this, "No food entered",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void initializeRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.SFDrecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void searchFood(final String foodName){
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
                    USDAFoodParser parser;
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
        SFDfoodEntries.clear();
        for(int i = 0; i < parserLocal.getFoodList().size(); i++) {
            SFDfoodEntries.add(parserLocal.getFoodList().get(i));
        }
        Log.d("SearchFoodDatabase", "adapterUpdated");
        adapter.notifyDataSetChanged();
    }

    public void openDialog() {
        SearchFoodDatabaseDialog dialog = new SearchFoodDatabaseDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");
    }
}