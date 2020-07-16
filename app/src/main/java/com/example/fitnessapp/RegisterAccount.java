package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public class RegisterAccount extends AppCompatActivity {
    EditText emailInput, passwordInput, usernameInput;
    Button registerAccountButton;
    DatabaseReference reff;
    Account account;

    private static final String TAG = "EmailPassword";

    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        registerAccountButton = (Button)findViewById((R.id.registerAccountButton));
        emailInput = (EditText)findViewById(R.id.emailText);
        passwordInput = (EditText)findViewById(R.id.passwordText);
        usernameInput = (EditText)findViewById(R.id.usernameText);

        account = new Account();
        reff = FirebaseDatabase.getInstance().getReference();

        reff.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid =(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                account.setEmail(emailInput.getText().toString().trim());
                account.setPassword(passwordInput.getText().toString().trim());
                account.setUsername(usernameInput.getText().toString().trim());

                //firebase.auth().createUserWithEmailandPassword(account.getEmail(), account.getPassword());

                //Toast.makeText(RegisterAccount.this, "yeet.", Toast.LENGTH_SHORT).show();
                createAccount();
                //reff.child(account.getUsername()).setValue(account);//accounts are now incremented and will push the data in account instance to database
                //reff.push().setValue(account);//push the data in account instance to database
                //Toast.makeText(RegisterAccount.this, "Account has been registered", Toast.LENGTH_LONG).show();

            }
        });



    }

    public void createAccount(){
        MainActivity.mAuth.createUserWithEmailAndPassword(account.getEmail(), account.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Create user with email: Success");
                    MainActivity.currentUser = MainActivity.mAuth.getCurrentUser();

                    //assign user id account a friend code
                    int leftLimit = 97; // letter 'a'
                    int rightLimit = 122; // letter 'z'
                    int targetStringLength = 10;
                    Random random = new Random();
                    StringBuilder buffer = new StringBuilder(targetStringLength);
                    for (int i = 0; i < targetStringLength; i++) {
                        int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
                        buffer.append((char) randomLimitedInt);
                    }
                    String uniqueKey = buffer.toString();

                    Log.d("UUID",uniqueKey);
                    reff.child("Users").child(MainActivity.currentUser.getUid()).child("Friends List Info").child("UUID").setValue(uniqueKey);
                    reff.child("Users").child(MainActivity.currentUser.getUid()).child("Friends List Info").child("Username").setValue(account.getUsername());
                    FriendsListContainer placeholder = new FriendsListContainer();
                    //placeholder.addFriend("placeholder");
                    //Map<String,Object> childUpdates = new HashMap<>();
                    //childUpdates.put("/"+MainActivity.currentUser.getUid()+"/Friends List/List/"+placeholder);
                    Log.d("friend list check",placeholder.toString());
                    reff.child("Users").child(MainActivity.currentUser.getUid()).child("Friends List Info").child("List").setValue(placeholder);
                    openMainActivity();
                } else {
                    //sign-in fails somehow
                    Log.w(TAG, "Create user with email: Failure");
                    Toast.makeText(RegisterAccount.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void openMainActivity(){//send user back to main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
