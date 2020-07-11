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
import android.widget.AdapterView;
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
import com.google.firebase.database.core.view.Change;

import java.util.ArrayList;
import java.util.List;

public class CalorieTracker extends AppCompatActivity {
    private boolean firstStart = true;
    final static String TAG = "CalorieTracker";
    protected static String currentLoginSession = "";
    protected static ArrayList<TrackerData> entries = new ArrayList<TrackerData>();
    final DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton addTrackerData, removeEntry, editEntry, cancelSelected;
    ListView trackerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        //create back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //reset entires if new user is logged in
        checkForNewUser();

        //pulls entries if needed and draws listView
        pullEntriesIfNeeded();


        //create floating button to add entries
        createTrackerDataButtons();

        //create item click listener
        registerClickCallback();
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

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.itemList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                showFABS();

                //if remove entry is selected, remove entry
                removeEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        entries.remove(position);
                        hideFABS();
                        pushEntriesToDatabase();
                        populateListView();

                        Toast.makeText(CalorieTracker.this, "\"" + entries.get(0).getFoodType() + "\"  has been removed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                editEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CalorieTracker.this, ChangeEntryData.class);
                        intent.putExtra("position", ""+ position);
                        startActivity(intent);
                    }
                });

            }
        });
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
                    if (dataSnapshot.child(MainActivity.currentUser.getUid()).child("entries").exists()) {
                        Log.d(TAG, "entries found");
                        //convert database object to ArrayList<TrackerData>
                        GenericTypeIndicator<ArrayList<TrackerData>> t = new GenericTypeIndicator<ArrayList<TrackerData>>() {
                        };
                        entries = dataSnapshot.child(MainActivity.currentUser.getUid()).child("entries").getValue(t);
                    } else {
                        Log.d(TAG, "class TrackerDataContainer not found");

                    }

                    //draw list on screen
                    if (entries != null) {
                        populateListView();
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
            populateListView();
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


        removeEntry = findViewById(R.id.removeEntry);
        //hide removeEntry until needed
        removeEntry.setVisibility(View.GONE);


        editEntry = findViewById(R.id.editEntry);
        //hide editEntry until needed
        editEntry.setVisibility(View.GONE);
        editEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
        reff.child(MainActivity.currentUser.getUid()).setValue(new TrackerDataContainer(CalorieTracker.entries));
    }

    private void hideFABS() {
        editEntry.setVisibility(View.GONE);
        removeEntry.setVisibility(View.GONE);
        cancelSelected.setVisibility(View.GONE);
        addTrackerData.setVisibility(View.VISIBLE);
    }

    private void showFABS() {
        editEntry.setVisibility(View.VISIBLE);
        removeEntry.setVisibility(View.VISIBLE);
        cancelSelected.setVisibility(View.VISIBLE);
        addTrackerData.setVisibility(View.GONE);
    }

}