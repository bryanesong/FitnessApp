package com.example.fitnessapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddTrackerData extends AppCompatActivity {
    EditText foodTypeInput, calorieInput, quantityInput, measurementInput;
    Button submitDataButton;
    DatabaseReference reff;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracker_data);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        submitDataButton = (Button)findViewById((R.id.addDataButton));
        foodTypeInput = (EditText)findViewById(R.id.foodInput);
        calorieInput = (EditText)findViewById(R.id.calorieInput);
        quantityInput = (EditText)findViewById(R.id.quantityInput);
        measurementInput = (EditText)findViewById(R.id.measurementInput);

       submitDataButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){

                //if no text areas are blank, add current entry to Entries
                if(!foodTypeInput.getText().toString().equals("") && !calorieInput.getText().toString().equals("") && !quantityInput.getText().toString().equals("") && !measurementInput.getText().toString().equals("")) {

                    //add entry
                    CalorieTracker.addEntries(new TrackerData(Integer.parseInt(calorieInput.getText().toString()), foodTypeInput.getText().toString(), Integer.parseInt(quantityInput.getText().toString()), measurementInput.getText().toString(), java.time.LocalDate.now().toString(), java.time.LocalTime.now().toString()));

                    Toast.makeText(AddTrackerData.this, "\"" + foodTypeInput.getText() + "\" food entry has been submitted.",
                            Toast.LENGTH_LONG).show();

                    //update online database
                    final DatabaseReference reff;
                    reff = FirebaseDatabase.getInstance().getReference();
                    reff.child(MainActivity.currentUser.getUid()).setValue(new TrackerDataContainer(CalorieTracker.entries));

                    clearInputText();

                } else {
                    Toast.makeText(AddTrackerData.this, "Not all blanks are filled!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearInputText() {
        foodTypeInput.setText("");
        calorieInput.setText("");
        quantityInput.setText("");
        measurementInput.setText("");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //return to previous activity
        Intent myIntent = new Intent(getApplicationContext(), CalorieTracker.class);
        startActivityForResult(myIntent, 123);
        return true;
    }
}