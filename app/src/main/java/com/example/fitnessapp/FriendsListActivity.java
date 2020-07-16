package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {
    private DatabaseReference reff;
    private Button addFriendCodeButton,getFriendCodeButton,addFriendButtonWithCode,backButtonForFriendCode;
    private TextView friendsListView;
    private ListView friendsLayout;
    private EditText friendAddCodeBar;
    private String currentUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        addFriendCodeButton = findViewById(R.id.addFriendButton);
        getFriendCodeButton = findViewById(R.id.getFriendKeyButton);
        friendsListView = findViewById(R.id.friendsList);
        friendsLayout = findViewById(R.id.friendListLayout);
        friendAddCodeBar = findViewById(R.id.addFriendCodeBar);
        addFriendButtonWithCode = findViewById(R.id.addFriendCodeButton);
        backButtonForFriendCode = findViewById(R.id.backButtonForFriendCode);

        //initially set text to be invisible in case they do have friends
        friendsListView.setVisibility(ViewGroup.INVISIBLE);
        friendAddCodeBar.setVisibility(ViewGroup.INVISIBLE);
        addFriendButtonWithCode.setVisibility(ViewGroup.INVISIBLE);
        backButtonForFriendCode.setVisibility(ViewGroup.INVISIBLE);
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.mAuth.getUid()).child("Friends List Info").child("List");

        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FriendsListContainer friendList = snapshot.getValue(FriendsListContainer.class);
                //check if friends list is empty
                if(friendList.isEmpty()){
                    friendsListView.setVisibility(ViewGroup.VISIBLE);
                    friendsListView.setText("You have no friends :(");
                }else {
                    //if friends list isnt empty then start listing friends

                    //temporary array used for testing
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add("temp friend 1");
                    temp.add("temp friend 2");
                    temp.add("temp friend 3");
                    temp.add("temp friend 4");
                    temp.add("temp friend 5");
                    temp.add("temp friend 6");

                    //will fill up the listView with friends
                    ArrayAdapter arrayAdapter = new ArrayAdapter(FriendsListActivity.this,android.R.layout.simple_list_item_1,friendList.getUsernameList());
                    friendsLayout.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FriendsListActivity","getting friendsListContainer: Failed.");
            }
            });

        addFriendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addFriendAction();
            }
        });

        getFriendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getFriendCodeAction();
            }
        });

        addFriendButtonWithCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addFriendWithCodeAction();
            }
        });

        backButtonForFriendCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){// this button still needs to be worked on
                friendsListView.setVisibility(ViewGroup.INVISIBLE);
                friendsLayout.setVisibility(ViewGroup.VISIBLE);
                addFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                getFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                backButtonForFriendCode.setVisibility(ViewGroup.INVISIBLE);
            }
        });

    }

    public void addFriendAction(){
        friendsListView.setVisibility(ViewGroup.INVISIBLE);
        friendsLayout.setVisibility(ViewGroup.INVISIBLE);
        friendAddCodeBar.setVisibility(ViewGroup.VISIBLE);
        addFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        getFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        addFriendButtonWithCode.setVisibility(ViewGroup.VISIBLE);

        addFriendButtonWithCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean successful = false;
                reff = FirebaseDatabase.getInstance().getReference();

                if(successful){
                    Toast.makeText(FriendsListActivity.this,"Friend has been added!",Toast.LENGTH_LONG);
                    friendsListView.setVisibility(ViewGroup.INVISIBLE);
                    friendsLayout.setVisibility(ViewGroup.VISIBLE);
                    addFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                    getFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                    backButtonForFriendCode.setVisibility(ViewGroup.INVISIBLE);
                    addFriendButtonWithCode.setVisibility(ViewGroup.INVISIBLE);
                    friendAddCodeBar.setVisibility(ViewGroup.INVISIBLE);


                }else{
                    Toast.makeText(FriendsListActivity.this,"Friend code does not exist, please try again.",Toast.LENGTH_LONG);
                }

            }
        });

    }

    public void getFriendCodeAction(){
        friendsLayout.setVisibility(ViewGroup.INVISIBLE);
        addFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        getFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        backButtonForFriendCode.setVisibility(ViewGroup.VISIBLE);

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.mAuth.getUid()).child("Friends List Info");

        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUID = snapshot.child("UUID").getValue().toString();
                friendsListView.setText("Your friend code is:\n"+currentUID+"");
                Log.w("FriendsListActivity","Successfully retrieved current user UUID: "+currentUID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FriendsListActivity","getting friendsListContainer: Failed.");
            }
        });



        friendsListView.setVisibility(ViewGroup.VISIBLE);

    }

    public void addFriendWithCodeAction(){
        String currentFriendCode = friendAddCodeBar.getText().toString();

    }
}