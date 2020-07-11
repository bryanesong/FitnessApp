package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeScreen extends AppCompatActivity {

    private Button logoutButton,workoutLogButton,calorieTrackerButton;
    AnimationDrawable spriteAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        logoutButton = (Button)findViewById(R.id.logoutButton);
        workoutLogButton = (Button)findViewById(R.id.workoutLogButton);
        calorieTrackerButton = (Button)findViewById(R.id.trackerButton);

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.mAuth.getInstance().signOut();
                openMainActivity();
            }
        });

        workoutLogButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openWorkoutLog();
            }
        });

        calorieTrackerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCalorieTracker();
            }
        });

        ImageView sprite = (ImageView)findViewById(R.id.spriteImage);
        sprite.setBackgroundResource(R.drawable.sprite_animation);
        spriteAnimation = (AnimationDrawable)sprite.getBackground();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        spriteAnimation.start();
    }

    public void openWorkoutLog(){
        Intent intent = new Intent(this,WorkoutLog.class);
        startActivity(intent);
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

