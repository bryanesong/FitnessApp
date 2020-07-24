package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeScreen extends AppCompatActivity {

    private Button logoutButton,workoutLogButton,calorieTrackerButton,friendsListButton,shopButton;
    AnimationDrawable spriteAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        logoutButton = (Button)findViewById(R.id.logoutButton);
        workoutLogButton = (Button)findViewById(R.id.workoutLogButton);
        calorieTrackerButton = (Button)findViewById(R.id.trackerButton);
        friendsListButton = (Button)findViewById(R.id.friendsListButton);
        shopButton = (Button)findViewById(R.id.shopButton);

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity.mAuth.getInstance().signOut();
                openMainActivity();
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openShopActivity();
            }
        });

        workoutLogButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openWorkoutLog();
            }
        });

        friendsListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFriendsList();
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

    public void openFriendsList(){
        Intent intent = new Intent(this,FriendsListActivity.class);
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

    public void openShopActivity(){
        Intent intent = new Intent(this,ShopActivity.class);
        startActivity(intent);
    }

}

