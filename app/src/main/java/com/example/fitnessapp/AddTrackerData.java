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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddTrackerData extends AppCompatActivity implements Serializable {
    ArrayList<String> measurementSuggestions = new ArrayList<String>(Arrays.asList("Cups","Gallons","Ounces","Pounds","Grams"));
    ArrayList<String> foodTypeSuggestions = new ArrayList<String>(Arrays.asList("Cheeseballs"));
    ArrayList<TrackerData> entries = new ArrayList<>();
    EditText foodTypeInput, calorieInput, quantityInput, measurementInput, dateInput, timeInput;
    FloatingActionButton submitDataButton, cancelDataButton, searchDataButton;
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

        Bundle args = getIntent().getBundleExtra("BUNDLE");
        entries = (ArrayList<TrackerData>)args.getSerializable("Food Entries");
        Log.d("AddTrackerData", "" + entries.size());

        //create buttons
        addButton();

        //create text boxes
        addText();

        //set time and date
        setTmeAndDate();

        //collects and submits text to database
        addSubmitListener();

        //closes current activity
        cancelDataListener();

        //opens SearchFoodDatabase activity
        searchDataListener();

        //creates autocomplete textboxes
        createAutoComplete(measurementTypeAuto, R.id.changeEntryMeasurementTypeInput, "measurement", measurementSuggestions);
        createAutoComplete(foodTypeAuto, R.id.changeEntryFoodTypeInput, "food", foodTypeSuggestions);

    }

    private void addButton() {
        submitDataButton = findViewById((R.id.addDataConfirmButton));
        cancelDataButton = findViewById((R.id.addDataCancelButton));
        searchDataButton = findViewById((R.id.addDataSearchItemButton));
    }

    private void addText() {
        calorieInput = (EditText)findViewById(R.id.changeEntryCalorieInput);
        quantityInput = (EditText)findViewById(R.id.changeEntryQuantityInput);
        dateInput = (EditText)findViewById(R.id.changeEntryDateInput);
        timeInput=(EditText)findViewById(R.id.changeEntryTimeInput);

        foodTypeInput = (AutoCompleteTextView)findViewById(R.id.changeEntryFoodTypeInput);
        measurementInput = (AutoCompleteTextView)findViewById(R.id.changeEntryMeasurementTypeInput);

    }


    private void clearInputText() {
        foodTypeInput.setText("");
        calorieInput.setText("");
        quantityInput.setText("");
        measurementInput.setText("");
        setTmeAndDate();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //return to previous activity
        Intent myIntent = new Intent(getApplicationContext(), CalorieTracker.class);
        startActivityForResult(myIntent, 123);
        return true;
    }

    private void addSubmitListener() {
        submitDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //if no text areas are blank, add current entry to Entries
                if(!foodTypeInput.getText().toString().equals("")
                        && !calorieInput.getText().toString().equals("")
                        && !quantityInput.getText().toString().equals("")
                        && !measurementInput.getText().toString().equals("")
                        && !dateInput.getText().toString().equals("")
                        && !timeInput.getText().toString().equals("")
                ) {

                    //add entry

                    entries.add(new TrackerData(
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
                    pushDataToDatabase();

                    clearInputText();

                } else {
                    Toast.makeText(AddTrackerData.this, "Not all blanks are filled!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void pushDataToDatabase() {
        final DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child("Users").child(MainActivity.currentUser.getUid()).child("Calorie Tracker Data").setValue(new TrackerDataContainer(entries));
    }

    private void createAutoComplete(AutoCompleteTextView box, int ID, String entryTrait, ArrayList<String> suggestionList) {
        box = findViewById(ID);

        for(TrackerData i : entries) {
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

    private void cancelDataListener() {
        cancelDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTrackerData.this, CalorieTracker.class);
                startActivity(intent);
            }
        });
    }

    private void searchDataListener() {
        searchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTrackerData.this, SearchFoodDatabase.class);
                Bundle args = new Bundle();
                args.putSerializable("Food Entries", (Serializable)entries);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });
    }

    private void setTmeAndDate() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        dateInput.setText(dateFormat.format(date));;
        timeInput.setText(timeFormat.format(date));;
    }

}