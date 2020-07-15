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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddTrackerData extends AppCompatActivity {
    ArrayList<String> measurementSuggestions = new ArrayList<String>(Arrays.asList("Cups","Gallons","Ounces","Pounds","Grams"));
    ArrayList<String> foodTypeSuggestions = new ArrayList<String>(Arrays.asList("Cheeseballs"));
    EditText foodTypeInput, calorieInput, quantityInput, measurementInput, dateInput, timeInput;
    Button submitDataButton;
    DatabaseReference reff;
    Account account;
    AutoCompleteTextView measurementTypeAuto, foodTypeAuto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracker_data);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //create buttons
        addButton();

        //create text boxes
        addText();

        //collects and submits text to database
        addSubmitListener();

        //creates autocomplete textboxes
        createAutoComplete(measurementTypeAuto, R.id.measurementInput, "measurement", measurementSuggestions);
        createAutoComplete(foodTypeAuto, R.id.foodInput, "food", foodTypeSuggestions);

    }

    private void addButton() {
        submitDataButton = (Button)findViewById((R.id.addDataButton));
    }

    private void addText() {
        foodTypeInput = (EditText)findViewById(R.id.foodInput);
        calorieInput = (EditText)findViewById(R.id.calorieInput);
        quantityInput = (EditText)findViewById(R.id.quantityInput);
        measurementInput = (EditText)findViewById(R.id.measurementInput);
        dateInput = (EditText)findViewById(R.id.dateInput);
        timeInput=(EditText)findViewById(R.id.timeInput);
    }


    private void clearInputText() {
        foodTypeInput.setText("");
        calorieInput.setText("");
        quantityInput.setText("");
        measurementInput.setText("");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        Date date = new Date();
        dateInput.setText(dateFormat.format(date));;
        timeInput.setText(timeFormat.format(date));;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //return to previous activity
        Intent myIntent = new Intent(getApplicationContext(), CalorieTracker.class);
        startActivityForResult(myIntent, 123);
        return true;
    }

    private void addSubmitListener() {
        submitDataButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){

                //if no text areas are blank, add current entry to Entries
                if(!foodTypeInput.getText().toString().equals("") && !calorieInput.getText().toString().equals("") && !quantityInput.getText().toString().equals("") && !measurementInput.getText().toString().equals("") && !dateInput.getText().toString().equals("") && !timeInput.getText().toString().equals("")) {

                    //add entry

                    CalorieTracker.addEntries(new TrackerData(
                            Integer.parseInt(calorieInput.getText().toString()),
                            foodTypeInput.getText().toString(),
                            Integer.parseInt(quantityInput.getText().toString()),
                            measurementInput.getText().toString(),
                            dateInput.getText().toString(),
                            timeInput.getText().toString()
                    ));

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

    private void createAutoComplete(AutoCompleteTextView box, int ID, String entryTrait, ArrayList<String> suggestionList) {
        box = findViewById(ID);

        for(TrackerData i : CalorieTracker.entries) {
            if(entryTrait.equals("measurement")) {
                if (!suggestionList.contains(i.getMeasurement())) {
                    suggestionList.add(i.getMeasurement());
                }
            } else if(entryTrait.equals("food")) {
                if (!suggestionList.contains(i.getFoodType())) {
                    suggestionList.add(i.getFoodType());
                }

            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, suggestionList);

        box.setThreshold(1);
        box.setAdapter(adapter);
    }

}