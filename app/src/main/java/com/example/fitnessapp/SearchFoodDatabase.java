package com.example.fitnessapp;

import androidx.appcompat.app.ActionBar;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFoodDatabase extends AppCompatActivity implements SearchFoodDatabaseViewAdapter.ListItemClickListener, SearchFoodDatabaseDialog.clickListener, Serializable {
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";

    private ArrayList<USDAFoodParser.FoodEntry> SFDfoodEntries= new ArrayList<>();
    private ArrayList<TrackerData> entries = new ArrayList<>();
    SearchFoodDatabaseViewAdapter adapter = new SearchFoodDatabaseViewAdapter(this, SFDfoodEntries, this);

    private Button inputButton;

    TextView inputFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food_database);

        //retrieve current entries in Calorie Tracker
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        entries = (ArrayList<TrackerData>)args.getSerializable("Food Entries");
        Log.d("SearchFoodDatabase", "" + entries.size());

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        createInputButton();

        createTextView();

        createInputButtonListener();

        initializeRecyclerView();


    }

    public boolean onOptionsItemSelected(MenuItem item){
        //return to previous activity
        Intent myIntent = new Intent(getApplicationContext(), CalorieTracker.class);
        startActivityForResult(myIntent, 123);
        return true;
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

    @Override
    public void onItemClick(int position) {
        //SFDfoodEntries.get(position);
        openDialog(position);
    }

    public void openDialog(int position) {
        SearchFoodDatabaseDialog dialog = new SearchFoodDatabaseDialog(this, SFDfoodEntries.get(position));
        dialog.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public void cancelItem() {
        Log.d("SearchFoodDatabase", "cancel");
    }

    @Override
    public void addItem(String servingSize, USDAFoodParser.FoodEntry curEntry) {
        Log.d("SearchFoodDatabase", "add, servings: " + servingSize);
        if(!servingSize.equals("")) {
            addFoodToDatabase(Integer.parseInt(servingSize), curEntry);
            Toast.makeText(SearchFoodDatabase.this, "\"" + curEntry.getFoodName() + "\" food entry has been submitted.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(SearchFoodDatabase.this, "No serving size inputted!",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void addFoodToDatabase(int servingSize, USDAFoodParser.FoodEntry curEntry) {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        TrackerData entry = new TrackerData(curEntry.getCalories(), curEntry.getFoodName(), 100*servingSize, "Grams", dateFormat.format(date), timeFormat.format(date));
        entries.add(entry);

        final DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("Users").child(MainActivity.currentUser.getUid()).child("Calorie Tracker Data").setValue(new TrackerDataContainer(entries));
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
}