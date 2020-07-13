package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;


import okhttp3.*;
import java.io.IOException;
import java.net.*;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutLog extends AppCompatActivity {
    private Button testButton;
    private EditText searchBar;
    private TextView JSONText;
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_log);

        testButton = findViewById(R.id.testButton);
        JSONText = (TextView) findViewById(R.id.textViewTest);
        searchBar = (EditText) findViewById(R.id.searchBarText);




        testButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("log_tag", "starting string request.");

                AsyncTask asyncTask = new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        OkHttpClient client = new OkHttpClient();
                        Log.d("test","https://api.nal.usda.gov/fdc/v1/foods/search?api_key="+apiKey+"&query="+searchBar.getText().toString().replace(" ","%20"));
                        Request request = new Request.Builder()
                                .url("https://api.nal.usda.gov/fdc/v1/foods/search?api_key="+apiKey+"&query="+searchBar.getText().toString().replace(" ","%20"))
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
                        JSONText.setText(o.toString());
                        //testing parsing JSON file.
                        try {
                            JSONObject obj = new JSONObject(o.toString());

                            JSONText.setText(obj.get("query").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.execute();

            }
        });




        // executing the request (adding to queue)


    }

    public void getRequestApi(){

    }
}