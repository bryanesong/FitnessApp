package com.example.fitnessapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEntryData extends AppCompatActivity {
    FloatingActionButton confirmButton ,cancelButton;
    EditText foodTypeInput, calorieInput, quantityInput, measurementInput, dateInput, timeInput;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_entry_data);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        position = Integer.parseInt(getIntent().getStringExtra("position"));
        //create buttons
        setupButtons();

        //create and display current data in text boxes
        setupTextBoxes();

        //create button listeners and manage data when button is clicked
        createButtonListeners();



    }

    public boolean onOptionsItemSelected(MenuItem item){
        //return to previous activity
        Intent myIntent = new Intent(getApplicationContext(), CalorieTracker.class);
        startActivityForResult(myIntent, 123);
        return true;
    }

    private void setupButtons() {
        confirmButton = findViewById((R.id.addDataConfirmButton));
        cancelButton = findViewById((R.id.addDataCancelButton));


    }

    private void setupTextBoxes() {
        foodTypeInput = (EditText)findViewById(R.id.changeEntryFoodTypeInput);
        calorieInput = (EditText)findViewById(R.id.changeEntryCalorieInput);
        quantityInput = (EditText)findViewById(R.id.changeEntryQuantityInput);
        measurementInput = (EditText)findViewById(R.id.changeEntryMeasurementTypeInput);
        dateInput = (EditText)findViewById(R.id.changeEntryDateInput);
        timeInput = (EditText)findViewById(R.id.changeEntryTimeInput);
        foodTypeInput.setText(CalorieTracker.entries.get(position).getFoodType());
        calorieInput.setText(""+ CalorieTracker.entries.get(position).getCalories());
        quantityInput.setText(""+ CalorieTracker.entries.get(position).getQuantity());
        measurementInput.setText(CalorieTracker.entries.get(position).getMeasurement());
        dateInput.setText(CalorieTracker.entries.get(position).getDate());
        timeInput.setText(CalorieTracker.entries.get(position).getTime());
    }

    private void createButtonListeners() {

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!foodTypeInput.getText().toString().equals("") && !calorieInput.getText().toString().equals("") && !quantityInput.getText().toString().equals("") && !measurementInput.getText().toString().equals("") && !dateInput.getText().toString().equals("") && !timeInput.getText().toString().equals("")) {

                    //add entry
                    CalorieTracker.entries.set(position, (new TrackerData(Integer.parseInt(calorieInput.getText().toString()), foodTypeInput.getText().toString(), Integer.parseInt(quantityInput.getText().toString()), measurementInput.getText().toString(), dateInput.getText().toString(), timeInput.getText().toString())));

                    Toast.makeText(ChangeEntryData.this, "\"" + foodTypeInput.getText() + "\" food entry has been editted.",
                            Toast.LENGTH_LONG).show();

                    //update online database
                    final DatabaseReference reff;
                    reff = FirebaseDatabase.getInstance().getReference();
                    reff.child(MainActivity.currentUser.getUid()).setValue(new TrackerDataContainer(CalorieTracker.entries));

                } else {
                    Toast.makeText(ChangeEntryData.this, "Not all blanks are filled!",
                            Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(ChangeEntryData.this, CalorieTracker.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeEntryData.this, CalorieTracker.class);
                startActivity(intent);
            }
        });
    }
}