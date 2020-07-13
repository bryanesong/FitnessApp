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

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkoutLog extends AppCompatActivity {
    private Button testButton;
    private EditText searchBar;
    private TextView tempText;
    final static String apiKey = "OtpdWCaIaKlnq3DXBs5VcVorVDopFLNaVrGLWT6i";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_log);

        testButton = findViewById(R.id.testButton);
        tempText = (TextView) findViewById(R.id.textViewTest);
        searchBar = (EditText) findViewById(R.id.searchBarText);




        testButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("log_tag", "starting string request.");

                AsyncTask asyncTask = new AsyncTask(){
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        OkHttpClient client = new OkHttpClient();
                        Log.d("test","https://api.nal.usda.gov/fdc/v1/foods/list    ?api_key="+apiKey+"&query="+searchBar.getText().toString().replace(" ","%20"));
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
                        tempText.setText(o.toString());
                    }
                }.execute();

            }
        });




        // executing the request (adding to queue)


    }

    public void getRequestApi(){

    }
}