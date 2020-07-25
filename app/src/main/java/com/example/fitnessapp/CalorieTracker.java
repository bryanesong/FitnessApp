package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class CalorieTracker extends AppCompatActivity implements CalorieTrackerViewAdapter.CTlistItemClickListener {
    private boolean firstStart = true;
    final static String TAG = "CalorieTracker";
    protected static String currentLoginSession = "";
    protected ArrayList<TrackerData> entries = new ArrayList<TrackerData>();
    final DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton addTrackerData, removeEntry, editEntry, cancelSelected, searchDataButton;
    TextView noEntryMessage, totalCalories;
    CalorieTrackerViewAdapter adapter = new CalorieTrackerViewAdapter(this, entries, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //create textView for no entries
        createNoEntryText();

        //create textView for total calories
        createTotalCalorieText();

        //reset entires if new user is logged in
        checkForNewUser();

        //update entries
        updateNoEntryTextAndCalories();

        //pulls entries if needed and draws listView
        pullEntriesIfNeeded();

        //create floating button to add entries
        createTrackerDataButtons();

        //populate view
        populateListView();


    }

    public void populateListView() {
        RecyclerView recyclerView = findViewById(R.id.CTrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void updateNoEntryTextAndCalories() {
        updateNoEntryText();
        setTotalCalories();
    }


    @Override
    public void onItemClick(final int position) {
        showFABS();

        //if remove entry is selected, remove entry
        removeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalorieTracker.this, "\"" + entries.get(position).getFoodType() + "\"  has been removed.",
                        Toast.LENGTH_SHORT).show();
                entries.remove(position);
                hideFABS();
                pushEntriesToDatabase();
                updateEverything();


            }
        });

        editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalorieTracker.this, ChangeEntryData.class);
                intent.putExtra("position", ""+ position);
                Bundle args = new Bundle();
                args.putSerializable("Food Entries", (Serializable)entries);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });

    }

    private void openAddTrackerData() {
        Intent intent = new Intent(CalorieTracker.this, AddTrackerData.class);
        Bundle args = new Bundle();
        args.putSerializable("Food Entries", (Serializable)entries);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivityForResult(myIntent, 123);
        return true;
    }

    private void checkForNewUser() {
        if (!currentLoginSession.equals("") && currentLoginSession != MainActivity.currentUser.getUid()) {
            entries.clear();
            Log.d(TAG, "new user detected");
            Log.d(TAG, MainActivity.currentUser.getUid());
        } else {
            currentLoginSession = MainActivity.currentUser.getUid();
        }
    }

    private void pullEntriesIfNeeded() {
        if (entries == null || entries.isEmpty()) {
            //if entries is not found, pull from server

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Calorie Tracker Data").child("entries").exists()) {
                        Log.d(TAG, "entries found");
                        //convert database object to ArrayList<TrackerData>
                        TrackerDataContainer foodList = dataSnapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Calorie Tracker Data").getValue(TrackerDataContainer.class);
                        entries = foodList.getEntries();


                    } else {
                        Log.d(TAG, "class TrackerDataContainer not found");

                    }

                    //draw list on screen
                    if (entries != null) {
                        Log.d(TAG, "Populate list");
                        updateEverything();
                    } else {
                        Log.d("CalorieTracker", "entries is null");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "data retrieval cancelled");
                }
            });


        } else {
            //else, redraw list
           updateEverything();

        }
    }

    private void createTrackerDataButtons() {
        addTrackerData = findViewById(R.id.addTrackerData);
        addTrackerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTrackerData();
            }
        });
        searchDataButton = findViewById(R.id.searchItemButton);
        searchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalorieTracker.this, SearchFoodDatabase.class);
                Bundle args = new Bundle();
                args.putSerializable("Food Entries", (Serializable)entries);
                intent.putExtra("BUNDLE", args);
                startActivity(intent);
            }
        });


        removeEntry = findViewById(R.id.removeEntry);
        //hide removeEntry until needed
        removeEntry.setVisibility(View.GONE);


        editEntry = findViewById(R.id.editEntry);
        //hide editEntry until needed
        editEntry.setVisibility(View.GONE);


        cancelSelected = findViewById(R.id.cancelSelected);
        //hide cancelSelected until needed
        cancelSelected.setVisibility(View.GONE);
        cancelSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFABS();
            }
        });
    }

    private void pushEntriesToDatabase() {
        //pushes entries to database
        reff.child("Users").child(MainActivity.currentUser.getUid()).child("Calorie Tracker Data").setValue(new TrackerDataContainer(entries));
    }

    private void hideFABS() {
        editEntry.setVisibility(View.GONE);
        removeEntry.setVisibility(View.GONE);
        cancelSelected.setVisibility(View.GONE);
        addTrackerData.setVisibility(View.VISIBLE);
        searchDataButton.setVisibility(View.VISIBLE);
    }

    private void showFABS() {
        editEntry.setVisibility(View.VISIBLE);
        removeEntry.setVisibility(View.VISIBLE);
        cancelSelected.setVisibility(View.VISIBLE);
        addTrackerData.setVisibility(View.GONE);
        searchDataButton.setVisibility(View.GONE);
    }


    private void createNoEntryText() {
        noEntryMessage = findViewById(R.id.noEntryTextView);
    }

    private void createTotalCalorieText() {
        totalCalories = findViewById(R.id.totalCalorieTextView);
        totalCalories.setBackgroundColor(Color.LTGRAY);
    }

    private void updateNoEntryText() {
        if(entries.isEmpty()) {
            noEntryMessage.setText("No entries found :(");
        } else {
            noEntryMessage.setText("");
        }
    }

    private void setTotalCalories() {
        int numOfCalories = 0;
        for(TrackerData t : entries) {
            numOfCalories += t.getCalories();
        }
        totalCalories.setText("Total Calories: " + numOfCalories);
    }

    private void updateEverything() {
        updateNoEntryTextAndCalories();
        orderEntries();
        adapter.loadEntriesList(createDayLabels());

    }

    private void orderEntries() {
        //sort entries by date
        Log.d(TAG, "Updated list");
        Log.d(TAG, "size: " + entries.size());
        Collections.sort(entries);
        pushEntriesToDatabase();

    }

    private ArrayList<TrackerData> createDayLabels() {
        ArrayList<TrackerData> tempArr = new ArrayList<>(entries);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        Calendar c = Calendar.getInstance();

        String[] daysOfWeek = {"", "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

        String prevDateOfDay = "";

        for(int i = 0; i < tempArr.size(); i++) {
            Log.d(TAG, tempArr.get(i).getDate() + " equals: " + (dateFormat.format(date)));
            Log.d(TAG, "current index: " + i);
            if(i==0 && !tempArr.get(i).isDateData() && tempArr.get(i).getDate().equals(dateFormat.format(date))) {
                tempArr.add(0, new TrackerData(true, "Today"));
                prevDateOfDay = dateFormat.format(date);
                i++;
            } else {
                if(!tempArr.get(i).isDateData() && !prevDateOfDay.equals(tempArr.get(i).getDate())) {

                    try {
                        c.setTime(dateFormat.parse(tempArr.get(i).getDate()));

                    }catch(Exception e) {
                        Log.d(TAG, e.toString());
                    }
                    prevDateOfDay = tempArr.get(i).getDate();
                    tempArr.add(i, new TrackerData(true, daysOfWeek[c.get(Calendar.DAY_OF_WEEK)] + " (" + prevDateOfDay + ")"));
                    Log.d(TAG, "day title added");

                }
            }
        }
        Log.d(TAG, "Size of returned arr: " + tempArr.size());
        return tempArr;

    }


}