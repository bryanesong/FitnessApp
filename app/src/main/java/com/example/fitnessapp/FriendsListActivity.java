package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {
    private DatabaseReference reff;
    private Button addFriendCodeButton,getFriendCodeButton;
    private TextView friendsListView;
    private LinearLayout friendsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        addFriendCodeButton = findViewById(R.id.addFriendButton);
        getFriendCodeButton = findViewById(R.id.getFriendKeyButton);
        friendsListView = findViewById(R.id.friendsList);
        friendsLayout = findViewById(R.id.friendLinearLayout);

        //initially set text to be invisible in case they do have friends
        friendsListView.setVisibility(ViewGroup.INVISIBLE);

        reff = FirebaseDatabase.getInstance().getReference().child(MainActivity.mAuth.getUid()).child("Friends List Info").child("List");

        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FriendsListContainer friendList = snapshot.getValue(FriendsListContainer.class);
                //check if friends list is empty
                if(false){//friendList.isEmpty()){
                    friendsListView.setVisibility(ViewGroup.VISIBLE);
                    friendsListView.setText("You have no friends :(");
                }else {

                    //if friends list isnt empty then start listing friends
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("temp friend 1");
                    temp.add("temp friend 2");
                    temp.add("temp friend 3");
                    temp.add("temp friend 4");
                    temp.add("temp friend 5");
                    temp.add("temp friend 6");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FriendsListActivity","getting friendsListContainer: Failed.");
            }
            });//used to read if the current user has a generated user id


    }
}