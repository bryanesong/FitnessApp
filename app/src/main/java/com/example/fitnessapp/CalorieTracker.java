package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CalorieTracker extends AppCompatActivity {
    private boolean firstStart = true;
    final String TAG = "CalorieTracker";
    static ArrayList<TrackerData> entries = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //push data to server
        //add data to database
        final DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        reff.child(MainActivity.currentUser.getUid());
        if(CalorieTracker.entries != null) {
            reff.setValue(new TrackerDataContainer(CalorieTracker.entries));
        }

        if(entries == null) {
            //if entries is not found, pull from server

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("entries").exists()) {
                        Log.d(TAG, "entries found");
                        //convert database object to ArrayList<TrackerData>
                        GenericTypeIndicator<ArrayList<TrackerData>> t = new GenericTypeIndicator<ArrayList<TrackerData>>() {
                        };
                        entries = dataSnapshot.child("entries").getValue(t);
                    } else {
                        //create and push new data container if nonexistent
                        Log.d(TAG, "class TrackerDataContainer not found");
                        TrackerDataContainer myDataContainer = new TrackerDataContainer();
                        myDataContainer.entries.add(new TrackerData(100, "mac", 16, "cups"));
                        reff.setValue(myDataContainer);


                    }

                    //draw list on screen
                    if (entries != null) {
                        populateListView();
                    } else {
                        Log.e("CalorieTracker", "entries is null");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "data retrieval cancelled");
                }
            });



        } else {
            //else, redraw list
            populateListView();
        }

        //create floating button to add entries
        FloatingActionButton fab = findViewById(R.id.addTrackerData);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTrackerData();
            }
        });
    }

    public void populateListView() {
        ArrayAdapter<TrackerData> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.itemList);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<TrackerData> {
        public MyListAdapter() {
            super(CalorieTracker.this, R.layout.item_view, entries);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate((R.layout.item_view), parent, false);
            }

            //find current entry
            TrackerData currentEntry = entries.get(position);
            Log.d("Yeet", "" + currentEntry.getQuantity());

            TextView setCalorieText = (TextView) itemView.findViewById(R.id.calorieText);
            setCalorieText.setText("" + currentEntry.getCalories() + " cal");

            TextView setFoodText = (TextView) itemView.findViewById(R.id.foodTypeText);
            setFoodText.setText(currentEntry.getFoodType());

            TextView setOtherInfoText = (TextView) itemView.findViewById(R.id.otherText);
            setOtherInfoText.setText("" + currentEntry.getQuantity() + " " + currentEntry.getMeasurement());
            return itemView;
        }
    }


    private void openAddTrackerData() {
        Intent intent = new Intent(CalorieTracker.this, AddTrackerData.class);
        startActivityForResult(intent, 0);
    }

    public static void addEntries(TrackerData newEntry) {
        entries.add(newEntry);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}