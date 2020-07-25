package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InventoryActivity extends AppCompatActivity {
    private ImageView itemOnPlayer_Hat,itemOnPlayer_Shirt,itemOnPlayer_Pants,itemOnPlayer_RightItem,itemOnPlayer_LeftItem,itemOnPlayer_Shoes;
    private DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initializeImageViews();//initalize all image views

        //get firebase database reference
        reff = FirebaseDatabase.getInstance().getReference();
        retrieveAndUpdateInventory(reff);

    }

    public void initializeImageViews(){
        itemOnPlayer_Hat = findViewById(R.id.itemOnPlayer_Hat);
        itemOnPlayer_Shirt = findViewById(R.id.itemOnPlayer_Shirt);
        itemOnPlayer_RightItem = findViewById(R.id.itemOnPlayer_RightItem);
        itemOnPlayer_LeftItem = findViewById(R.id.itemOnPlayer_LeftItem);
        itemOnPlayer_Shoes = findViewById(R.id.itemOnPlayer_Shoes);
    }

    //this method will reach out and grab all iventory that the player has currently equipped
    private void retrieveAndUpdateInventory(DatabaseReference reference){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                InventoryInfoContainer inventory = snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").getValue(InventoryInfoContainer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}