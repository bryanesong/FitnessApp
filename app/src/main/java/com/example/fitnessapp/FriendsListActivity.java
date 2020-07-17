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
import java.util.Iterator;

public class FriendsListActivity extends AppCompatActivity {
    private DatabaseReference reff;
    private Button addFriendCodeButton,getFriendCodeButton,addFriendButtonWithCode,backButtonForFriendCode;
    private TextView friendsListView;
    private ListView friendsLayout;
    private EditText friendAddCodeBar;
    private String currentUID = "";
    private boolean foundFriendSuccess;
    //ArrayList<String> entries = new ArrayList<>();

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
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

        populateFriendsList(reff);

        addFriendCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addFriendAction(reff);
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
                friendsLayout.setVisibility(ViewGroup.VISIBLE);
                addFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                getFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                backButtonForFriendCode.setVisibility(ViewGroup.INVISIBLE);
                friendsListView.setVisibility(ViewGroup.INVISIBLE);
                populateFriendsList(reff);
            }
        });

    }

    public void populateFriendsList(DatabaseReference reference){
        reference = reference.child(MainActivity.currentUser.getUid()).child("Friends List Info").child("List");
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FriendsListContainer friendList = snapshot.getValue(FriendsListContainer.class);
                //check if friends list is empty
                if(friendList.isEmpty()){
                    friendsListView.setVisibility(ViewGroup.VISIBLE);
                    friendsListView.setText("You have no friends :(");
                }else {
                    //if friends list isnt empty then start listing friends
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
    }

    public void addFriendAction(final DatabaseReference reference){
        friendsListView.setVisibility(ViewGroup.INVISIBLE);
        friendsLayout.setVisibility(ViewGroup.INVISIBLE);
        friendAddCodeBar.setVisibility(ViewGroup.VISIBLE);
        addFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        getFriendCodeButton.setVisibility(ViewGroup.INVISIBLE);
        addFriendButtonWithCode.setVisibility(ViewGroup.VISIBLE);
        foundFriendSuccess = false;
        addFriendButtonWithCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(friendAddCodeBar.getText().toString().equals("")){
                    Toast.makeText(FriendsListActivity.this,"Friend Code is empty, please type in friend code.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(friendAddCodeBar.getText().toString().equals(reference.getKey())){
                    Toast.makeText(FriendsListActivity.this,"Cannot add yourself, please try again.",Toast.LENGTH_SHORT).show();
                    friendAddCodeBar.getText().clear();
                    return;
                }

                //search through database to check if friend code exists, if does, then add to user friendlist
                reference.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterator<DataSnapshot> users = snapshot.getChildren().iterator();
                        Toast.makeText(FriendsListActivity.this,"Total Users: "+snapshot.getChildrenCount(),Toast.LENGTH_LONG);
                        int count = 0;
                        while(users.hasNext()){
                            //use for debugging---------------
                            Log.d("searching","count: "+count);
                            count++;
                            //--------------------
                            DataSnapshot user = users.next();

                            Log.d("user's firebase key ",user.getKey().toString());
                            Log.d("Friends info compare","friend code bar: "+friendAddCodeBar.getText().toString());
                            Log.d("Friends info compare","firebase friend code: "+user.child("Friends List Info").child("UUID").getValue().toString());

                            if(user.child("Friends List Info").child("UUID").getValue().toString().equals(friendAddCodeBar.getText().toString())){
                                foundFriendSuccess = true;
                                //once user has been found grab friendlistcontainer from current user and update and repush into firebase database
                                FriendsListContainer friendListTempCurrentUser = snapshot.child(MainActivity.currentUser.getUid()).child("Friends List Info").child("List").getValue(FriendsListContainer.class);
                                if(friendListTempCurrentUser.containsFriendUserId(user.getKey())){
                                    Log.d("USER_ADD_FRIEND","Friend is already part of user friend list.");
                                    Log.d("USER_ADD_FRIEND","Friends:"+friendListTempCurrentUser.toString());
                                    Toast.makeText(FriendsListActivity.this,"Friend is already part of your friends list.",Toast.LENGTH_LONG).show();
                                }else{
                                    friendListTempCurrentUser.addFriend(user.getKey(),snapshot.child(user.getKey()).child("Friends List Info").child("Username").getValue().toString());
                                    reference.child(MainActivity.currentUser.getUid()).child("Friends List Info").child("List").setValue(friendListTempCurrentUser);
                                    Toast.makeText(FriendsListActivity.this,"Friend has been added!",Toast.LENGTH_LONG).show();
                                }
                                friendAddCodeBar.getText().clear();
                                break;
                            }
                        }
                        if(foundFriendSuccess == true){
                            Log.d("user add friend","user friend has been found.");
                            friendsListView.setVisibility(ViewGroup.INVISIBLE);
                            friendsLayout.setVisibility(ViewGroup.VISIBLE);
                            addFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                            getFriendCodeButton.setVisibility(ViewGroup.VISIBLE);
                            backButtonForFriendCode.setVisibility(ViewGroup.INVISIBLE);
                            addFriendButtonWithCode.setVisibility(ViewGroup.INVISIBLE);
                            friendAddCodeBar.setVisibility(ViewGroup.INVISIBLE);
                        }else{
                            Log.d("user add friend","user friend has NOT been found.");
                            Toast.makeText(FriendsListActivity.this,"Friend code does not exist, please try again.",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("FriendsListActivity","getting friendslist UUID: Failed.");
                    }
                });
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
                friendsListView.setTextSize(40);
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