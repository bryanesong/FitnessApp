package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsListActivity extends AppCompatActivity {
    private DatabaseReference reff;
    private Button addFriendCodeButton,getFriendCodeButton;
    private TextView friendsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        addFriendCodeButton = findViewById(R.id.addFriendButton);
        getFriendCodeButton = findViewById(R.id.getFriendKeyButton);
        friendsListView = findViewById(R.id.friendsList);

        reff = FirebaseDatabase.getInstance().getReference().child(MainActivity.mAuth.getUid()).child("FriendsList");

        ValueEventListener postListen = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FriendsListContainer friendList = snapshot.getValue(FriendsListContainer.class);
                //Log.d
                if(friendList.isEmpty()){
                    friendsListView.setText("You have no friends :(");
                }else{
                    friendsListView.setText("You have some friends!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FriendsListActivity","getting friendsListContainer: Failed.");
            }
        };//used to read if the current user has a generated user id


    }
}