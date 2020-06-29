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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private Button registerButton, loginButton;
    private EditText emailInput, passwordInput;
    DatabaseReference reff;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = mAuth.getCurrentUser();
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton =(Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        emailInput = (EditText)findViewById(R.id.emailText);
        passwordInput = (EditText)findViewById(R.id.passwordText);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signInWithEmailAndPassword();

                /*
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
                */
            }
        });

        //check to see if current user is already signed in
        //mAuth = FirebaseAuth.getInstance();
        //currentUser = mAuth.getCurrentUser();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Toast.makeText(MainActivity.this, "User is logged in.", Toast.LENGTH_LONG).show();
            openHomeScreen();
        }else{
            Toast.makeText(MainActivity.this, "Signed Out.", Toast.LENGTH_LONG).show();
        }

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

    public void signInWithEmailAndPassword(){
        Log.d(TAG,"username: "+emailInput.getText().toString());
        Log.d(TAG,"password: "+ passwordInput.getText().toString().trim());
        mAuth.signInWithEmailAndPassword(emailInput.getText().toString().trim(),passwordInput.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Sign in with Email: Success");
                    currentUser = mAuth.getCurrentUser();

                }else{
                    Log.w(TAG,"Sign in with Email: Failure");
                    Toast.makeText(MainActivity.this, "Authentication Failed.",Toast.LENGTH_SHORT).show();
                }
            }

        });
        openHomeScreen();
    }

    public FirebaseUser getUser(){
        return currentUser;
    }

    public FirebaseAuth getAuth(){
        return mAuth;
    }

}
