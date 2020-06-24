package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private Button registerButton, loginButton;
    private EditText usernameInput, passwordInput;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton =(Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameInput = (EditText)findViewById(R.id.usernameText);
        passwordInput = (EditText)findViewById(R.id.passwordText);


        Toast.makeText(MainActivity.this, "Firebase connection success", Toast.LENGTH_LONG).show();

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String currentUsername = usernameInput.getText().toString().trim();

                reff = FirebaseDatabase.getInstance().getReference().child("Account").child(currentUsername);

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currentPassword = passwordInput.getText().toString().trim();
                        String password = dataSnapshot.child("password").getValue().toString();//will retrieve the password for the corresponding username entered in the edittext box
                        Log.d("FitnessApp1234567","comparing: " + password +" and " +currentPassword);//debugging
                        if(password.equals(currentPassword)){
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                            openHomeScreen();
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    //this method will run if the login password matches the username;
    public void openHomeScreen(){
        Intent intent2 = new Intent(this,HomeScreen.class);
        startActivity(intent2);
    }

    //this method will open up the register activity
    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterAccount.class);
        startActivity(intent);
    }

}
