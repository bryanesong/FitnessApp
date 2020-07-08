package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalorieTracker extends AppCompatActivity {
    ArrayList<TrackerData> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        entries.add(new TrackerData(100, "mac", 16, "cups"));
        entries.add(new TrackerData(1000, "poo", 24, "gallons"));

        //create back bar

        /*DatabaseReference reff;
        reff = FirebaseDatabase.getInstance().getReference();
        TrackerDataContainer yeet = new TrackerDataContainer();
        reff.child(MainActivity.currentUser.getUid()).setValue(yeet);*/

        /*reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("password").getValue().toString() == null) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        populateListView();
    }

    private void populateListView(){
        ArrayAdapter<TrackerData> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.itemList);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<TrackerData>{
        public MyListAdapter() {
            super(CalorieTracker.this, R.layout.item_view, entries);
        }

        public View getView ( int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate((R.layout.item_view), parent, false);
            }

            //find current entry
            TrackerData currentEntry = entries.get(position);
            Log.d("Yeet", "" + currentEntry.getQuantity());

            TextView setCalorieText = (TextView) itemView.findViewById(R.id.calorieText);
            setCalorieText.setText("" + currentEntry.getCalories());

            TextView setFoodText = (TextView) itemView.findViewById(R.id.foodTypeText);
            setFoodText.setText(currentEntry.getFoodType());

            TextView setOtherInfoText = (TextView) itemView.findViewById(R.id.otherText);
            setOtherInfoText.setText("" + currentEntry.getQuantity() + " " + currentEntry.getMeasurement());
            return itemView;
        }
    }

    /*public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivityForResult(myIntent, 0);
        return true;
    }*/

}