package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {
    private Button logoutButton;
    private Button trackerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        trackerButton = (Button)findViewById(R.id.calorieTracker);
        trackerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openCalorieTracker();
            }
        });

        logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.mAuth.getInstance().signOut();
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void openCalorieTracker(){
        Intent intent = new Intent(this,CalorieTracker.class);
        startActivity(intent);
    }
}
