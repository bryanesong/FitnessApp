package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InventoryActivity extends AppCompatActivity {
    private ImageView itemOnPlayer_Hat,itemOnPlayer_Shirt,itemOnPlayer_Pants,itemOnPlayer_RightItem,itemOnPlayer_LeftItem,itemOnPlayer_Shoes;
    private DatabaseReference reff;
    private android.widget.GridView grid;

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
                //InventoryInfoContainer inventory = snapshot.child("Users").child(MainActivity.currentUser.getUid()).child("Inventory Info").getValue(InventoryInfoContainer.class);
                //int numItems = inventory.getItems().size();
                String[] itemName = new String[5];
                final int[] imageId = new int[5];
                itemName[0] = "temp1";
                itemName[1] = "temp2";
                itemName[2] = "temp3";
                itemName[3] = "temp4";
                itemName[4] = "temp5";

                imageId[0] = R.drawable.simp_card;
                imageId[1] = R.drawable.simp_tank_top;
                imageId[2] = R.drawable.gamer_juice;
                imageId[3] = R.drawable.gaymers_rise_up;
                imageId[4] = R.drawable.ab_vibrator;
                /*
                for(int i = 0;i<numItems;i++){
                    itemName[i] = inventory.getItems().get(i).getName();
                    imageId[i] = inventory.getItems().get(i).getImageResource();
                }
                */
                InventoryCustomGrid adapter = new InventoryCustomGrid(InventoryActivity.this, itemName, imageId);
                grid=findViewById(R.id.grid);
                grid.setAdapter(adapter);
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(InventoryActivity.this, "You Clicked at " +imageId[+ position], Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}