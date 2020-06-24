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


public class RegisterAccount extends AppCompatActivity {
    EditText emailInput, passwordInput, usernameInput;
    Button registerAccountButton;
    DatabaseReference reff;
    Account account;

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
        reff = FirebaseDatabase.getInstance().getReference().child("Account");

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

                reff.child(account.getUsername()).setValue(account);//accounts are now incremented and will push the data in account instance to database
                //reff.push().setValue(account);//push the data in account instance to database
                Toast.makeText(RegisterAccount.this, "Account has been registered", Toast.LENGTH_LONG).show();
                openMainActivity();
            }
        });


    }

    public void openMainActivity(){//send user back to main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
